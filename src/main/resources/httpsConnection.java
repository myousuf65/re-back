   1: /* HttpsURLConnection.java -- an HTTPS connection.
   2:    Copyright (C) 2004, 2006 Free Software Foundation, Inc.
   3: 
   4: This file is part of GNU Classpath.
   5: 
   6: GNU Classpath is free software; you can redistribute it and/or modify
   7: it under the terms of the GNU General Public License as published by
   8: the Free Software Foundation; either version 2, or (at your option)
   9: any later version.
  10: 
  11: GNU Classpath is distributed in the hope that it will be useful, but
  12: WITHOUT ANY WARRANTY; without even the implied warranty of
  13: MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  14: General Public License for more details.
  15: 
  16: You should have received a copy of the GNU General Public License
  17: along with GNU Classpath; see the file COPYING.  If not, write to the
  18: Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
  19: 02110-1301 USA.
  20: 
  21: Linking this library statically or dynamically with other modules is
  22: making a combined work based on this library.  Thus, the terms and
  23: conditions of the GNU General Public License cover the whole
  24: combination.
  25: 
  26: As a special exception, the copyright holders of this library give you
  27: permission to link this library with independent modules to produce an
  28: executable, regardless of the license terms of these independent
  29: modules, and to copy and distribute the resulting executable under
  30: terms of your choice, provided that you also meet, for each linked
  31: independent module, the terms and conditions of the license of that
  32: module.  An independent module is a module which is not derived from
  33: or based on this library.  If you modify this library, you may extend
  34: this exception to your version of the library, but you are not
  35: obligated to do so.  If you do not wish to do so, delete this
  36: exception statement from your version. */
  37: 
  38: 
  39: package javax.net.ssl;
  40: 
  41: import java.io.IOException;
  42: import java.net.HttpURLConnection;
  43: import java.net.URL;
  44: import java.security.Principal;
  45: import java.security.cert.Certificate;
  46: import java.security.cert.X509Certificate;
  47: 
  48: /**
  49:  * A URL connection that connects via the <i>Secure Socket Layer</i>
  50:  * (<b>SSL</b>) for HTTPS connections.
  51:  *
  52:  * <p>This class may be used in the same way as {@link
  53:  * HttpURLConnection}, and it will transparently negotiate the SSL
  54:  * connection.
  55:  *
  56:  * @author Casey Marshall (rsdio@metastatic.org)
  57:  */
  58: public abstract class HttpsURLConnection extends HttpURLConnection
  59: {
  60: 
  61:   // Fields.
  62:   // ------------------------------------------------------------------
  63: 
  64:   /**
  65:    * The default verifier.
  66:    * This is lazily initialized as required.
  67:    * @see #getDefaultHostnameVerifier
  68:    */
  69:   private static HostnameVerifier defaultVerifier;
  70: 
  71:   /**
  72:    * The default factory.
  73:    * This is lazily initialized as required.
  74:    * @see #getDefaultSSLSocketFactory
  75:    */
  76:   private static SSLSocketFactory defaultFactory;
  77: 
  78:   /**
  79:    * The hostname verifier used for this connection.
  80:    */
  81:   protected HostnameVerifier hostnameVerifier;
  82: 
  83:   /**
  84:    * This connection's socket factory.
  85:    */
  86:   private SSLSocketFactory factory;
  87: 
  88:   // Constructor.
  89:   // ------------------------------------------------------------------
  90: 
  91:   /**
  92:    * Creates a new HTTPS URL connection.
  93:    *
  94:    * @param url The URL of the connection being established.
  95:    * @specnote This was marked as throwing IOException in 1.4,
  96:    * but this was removed in 1.5.
  97:    */
  98:   protected HttpsURLConnection(URL url)
  99:   {
 100:     super(url);
 101:   }
 102: 
 103:   // Class methods.
 104:   // ------------------------------------------------------------------
 105: 
 106:   /**
 107:    * Returns the default hostname verifier used in all new
 108:    * connections.
 109:    * If the default verifier has not been set, a new default one will be
 110:    * provided by this method.
 111:    *
 112:    * @return The default hostname verifier.
 113:    */
 114:   public static synchronized HostnameVerifier getDefaultHostnameVerifier()
 115:   {
 116:     if (defaultVerifier == null)
 117:       {
 118:         defaultVerifier = new TrivialHostnameVerifier();
 119:       }
 120:     return defaultVerifier;
 121:   }
 122: 
 123:   /**
 124:    * Sets the default hostname verifier to be used in all new
 125:    * connections.
 126:    *
 127:    * @param newDefault The new default hostname verifier.
 128:    * @throws IllegalArgumentException If <i>newDefault</i> is null.
 129:    * @throws SecurityException If there is a security manager
 130:    *   currently installed and the caller does not have the {@link
 131:    *   SSLPermission} "setHostnameVerifier".
 132:    */
 133:   public static void setDefaultHostnameVerifier(HostnameVerifier newDefault)
 134:   {
 135:     if (newDefault == null)
 136:       throw new IllegalArgumentException("default verifier cannot be null");
 137:     SecurityManager sm = System.getSecurityManager();
 138:     if (sm != null)
 139:       sm.checkPermission(new SSLPermission("setHostnameVerifier"));
 140:     synchronized (HttpsURLConnection.class)
 141:       {
 142:         defaultVerifier = newDefault;
 143:       }
 144:   }
 145: 
 146:   /**
 147:    * Returns the default SSL socket factory used in all new
 148:    * connections.
 149:    * If the default SSL socket factory has not been set, a new default one
 150:    * will be provided by this method.
 151:    *
 152:    * @return The default SSL socket factory.
 153:    */
 154:   public static synchronized SSLSocketFactory getDefaultSSLSocketFactory()
 155:   {
 156:     if (defaultFactory == null)
 157:       {
 158:         try
 159:           {
 160:             defaultFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
 161:           }
 162:         catch (Throwable t)
 163:           {
 164:             t.printStackTrace();
 165:           }
 166:       }
 167:     return defaultFactory;
 168:   }
 169: 
 170:   /**
 171:    * Sets the default SSL socket factory to be used in all new
 172:    * connections.
 173:    *
 174:    * @param newDefault The new socket factory.
 175:    * @throws IllegalArgumentException If <i>newDefault</i> is null.
 176:    * @throws SecurityException If there is a security manager
 177:    *   installed and a call to {@link
 178:    *   SecurityManager#checkSetFactory()} fails.
 179:    */
 180:   public static void setDefaultSSLSocketFactory(SSLSocketFactory newDefault)
 181:   {
 182:     if (newDefault == null)
 183:       throw new IllegalArgumentException("default factory cannot be null");
 184:     SecurityManager sm = System.getSecurityManager();
 185:     if (sm != null)
 186:       sm.checkSetFactory();
 187:     synchronized (HttpsURLConnection.class)
 188:       {
 189:         defaultFactory = newDefault;
 190:       }
 191:   }
 192: 
 193:   // Instance methods.
 194:   // ------------------------------------------------------------------
 195: 
 196:   /**
 197:    * Returns the current hostname verifier for this instance.
 198:    *
 199:    * @return The hostname verifier.
 200:    */
 201:   public HostnameVerifier getHostnameVerifier()
 202:   {
 203:     if (hostnameVerifier == null)
 204:       {
 205:         hostnameVerifier = getDefaultHostnameVerifier();
 206:       }
 207:     return hostnameVerifier;
 208:   }
 209: 
 210:   /**
 211:    * Sets the hostname verifier for this instance.
 212:    *
 213:    * @param hostnameVerifier The new verifier.
 214:    * @throws IllegalArgumentException If <i>hostnameVerifier</i> is
 215:    *   null.
 216:    */
 217:   public void setHostnameVerifier(HostnameVerifier hostnameVerifier)
 218:   {
 219:     if (hostnameVerifier == null)
 220:       throw new IllegalArgumentException("verifier cannot be null");
 221:     this.hostnameVerifier = hostnameVerifier;
 222:   }
 223: 
 224:   /**
 225:    * Returns the current SSL socket factory for this instance.
 226:    *
 227:    * @return The current SSL socket factory.
 228:    */
 229:   public SSLSocketFactory getSSLSocketFactory()
 230:   {
 231:     if (factory == null)
 232:       {
 233:         factory = getDefaultSSLSocketFactory();
 234:       }
 235:     return factory;
 236:   }
 237: 
 238:   /**
 239:    * Sets the SSL socket factory for this instance.
 240:    *
 241:    * @param factory The new factory.
 242:    * @throws IllegalArgumentException If <i>factory</i> is null.
 243:    */
 244:   public void setSSLSocketFactory(SSLSocketFactory factory)
 245:   {
 246:     if (factory == null)
 247:       throw new IllegalArgumentException("factory cannot be null");
 248:     this.factory = factory;
 249:   }
 250: 
 251:   /**
 252:    * Returns the local principal for this connection.
 253:    *
 254:    * <p>The default implementation will return the {@link
 255:    * javax.security.x500.X500Principal} for the end entity certificate
 256:    * in the local certificate chain if those certificates are of type
 257:    * {@link java.security.cert.X509Certificate}. Otherwise, this
 258:    * method returns <code>null</code>.
 259:    *
 260:    * @return The local principal.
 261:    * @since 1.5
 262:    */
 263:   public Principal getLocalPrincipal ()
 264:   {
 265:     Certificate[] c = getLocalCertificates ();
 266:     if (c != null && c.length > 0 && (c[0] instanceof X509Certificate))
 267:       return ((X509Certificate) c[0]).getSubjectX500Principal ();
 268:     return null;
 269:   }
 270: 
 271:   /**
 272:    * Returns the remote peer's principal for this connection.
 273:    *
 274:    * <p>The default implementation will return the {@link
 275:    * javax.security.x500.X500Principal} for the end entity certificate
 276:    * in the remote peer's certificate chain if those certificates are
 277:    * of type {@link java.security.cert.X509Certificate}. Otherwise,
 278:    * this method returns <code>null</code>.
 279:    *
 280:    * @return The remote principal.
 281:    * @throws SSLPeerUnverifiedException If the remote peer has not
 282:    * been verified.
 283:    * @since 1.5
 284:    */
 285:   public Principal getPeerPrincipal () throws SSLPeerUnverifiedException
 286:   {
 287:     Certificate[] c = getServerCertificates ();
 288:     if (c != null && c.length > 0 && (c[0] instanceof X509Certificate))
 289:       return ((X509Certificate) c[0]).getSubjectX500Principal ();
 290:     return null;
 291:   }
 292: 
 293:   // Abstract methods.
 294:   // -------------------------------------------------------------------
 295: 
 296:   /**
 297:    * Returns the cipher name negotiated for this connection.
 298:    *
 299:    * @return The cipher name.
 300:    * @throws IllegalStateException If the connection has not yet been
 301:    *   established.
 302:    */
 303:   public abstract String getCipherSuite();
 304: 
 305:   /**
 306:    * Returns the certificates used on the local side in this
 307:    * connection.
 308:    *
 309:    * @return The local certificates.
 310:    * @throws IllegalStateException If the connection has not yet been
 311:    *  established.
 312:    */
 313:   public abstract Certificate[] getLocalCertificates();
 314: 
 315:   /**
 316:    * Returns the certificates sent by the other party.
 317:    *
 318:    * @return The peer's certificates.
 319:    * @throws IllegalStateException If the connection has not yet been
 320:    *   established.
 321:    * @throws SSLPeerUnverifiedException If the peer could not be
 322:    *   verified.
 323:    */
 324:   public abstract Certificate[] getServerCertificates() throws SSLPeerUnverifiedException;
 325: }
