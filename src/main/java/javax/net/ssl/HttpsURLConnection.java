    /* HttpsURLConnection.java -- an HTTPS connection.
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
  
   package javax.net.ssl;
  
  import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

  public abstract class HttpsURLConnection extends HttpURLConnection
   {
  
  
    private static HostnameVerifier defaultVerifier;

     private static SSLSocketFactory defaultFactory;
 
     
     protected HostnameVerifier hostnameVerifier;

    private SSLSocketFactory factory;
 
    
     protected HttpsURLConnection(URL url)
     {
     super(url);
   }

    public static synchronized HostnameVerifier getDefaultHostnameVerifier()
    {
      if (defaultVerifier == null)
        {
         // defaultVerifier = new TrivialHostnameVerifier();
       }
      return defaultVerifier;
    }

    
 public static void setDefaultHostnameVerifier(HostnameVerifier newDefault)
   {
      if (newDefault == null)
       throw new IllegalArgumentException("default verifier cannot be null");
     SecurityManager sm = System.getSecurityManager();
      if (sm != null)
        sm.checkPermission(new SSLPermission("setHostnameVerifier"));
      synchronized (HttpsURLConnection.class)
        {
        defaultVerifier = newDefault;
        }
    }
 
    public static synchronized SSLSocketFactory getDefaultSSLSocketFactory()
    {
      if (defaultFactory == null)
        {         try
           {
             defaultFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            }
          catch (Throwable t)
            {
              t.printStackTrace();
            }
        }
      return defaultFactory;
    }
  
    /**
 171:    * Sets the default SSL socket factory to be used in all new
 172:    * connections.
 173:    *
 174:    * @param newDefault The new socket factory.
 175:    * @throws IllegalArgumentException If <i>newDefault</i> is null.
 176:    * @throws SecurityException If there is a security manager
 177:    *   installed and a call to {@link
 178:    *   SecurityManager#checkSetFactory()} fails.
 179:    */
    public static void setDefaultSSLSocketFactory(SSLSocketFactory newDefault)
    {
      if (newDefault == null)
        throw new IllegalArgumentException("default factory cannot be null");
      SecurityManager sm = System.getSecurityManager();
     if (sm != null)
        sm.checkSetFactory();
      synchronized (HttpsURLConnection.class)
        {
         defaultFactory = newDefault;
        }
    }
 
   /**
 197:    * Returns the current hostname verifier for this instance.
 198:    *
 199:    * @return The hostname verifier.
 200:    */
    public HostnameVerifier getHostnameVerifier()
    {
     if (hostnameVerifier == null)
        {
          hostnameVerifier = getDefaultHostnameVerifier();
        }
      return hostnameVerifier;
    }
  
   /**
 211:    * Sets the hostname verifier for this instance.
 212:    *
 213:    * @param hostnameVerifier The new verifier.
 214:    * @throws IllegalArgumentException If <i>hostnameVerifier</i> is
 215:    *   null.
 216:    */
    public void setHostnameVerifier(HostnameVerifier hostnameVerifier)
    {
      if (hostnameVerifier == null)
        throw new IllegalArgumentException("verifier cannot be null");
      this.hostnameVerifier = hostnameVerifier;
    }
  
    /**
 225:    * Returns the current SSL socket factory for this instance.
 226:    *
 227:    * @return The current SSL socket factory.
 228:    */
    public SSLSocketFactory getSSLSocketFactory()
    {
      if (factory == null)
        {
         factory = getDefaultSSLSocketFactory();
        }
      return factory;
    }
  
   /**
 239:    * Sets the SSL socket factory for this instance.
 240:    *
 241:    * @param factory The new factory.
 242:    * @throws IllegalArgumentException If <i>factory</i> is null.
 243:    */
    public void setSSLSocketFactory(SSLSocketFactory factory)   {
     if (factory == null)
   throw new IllegalArgumentException("factory cannot be null");
     this.factory = factory;
    }

    public Principal getLocalPrincipal ()
    {
      Certificate[] c = getLocalCertificates ();
      if (c != null && c.length > 0 && (c[0] instanceof X509Certificate))
        return ((X509Certificate) c[0]).getSubjectX500Principal ();
      return null;
    }
 
 
    public Principal getPeerPrincipal () throws SSLPeerUnverifiedException
    {
      Certificate[] c = getServerCertificates ();
      if (c != null && c.length > 0 && (c[0] instanceof X509Certificate))
        return ((X509Certificate) c[0]).getSubjectX500Principal ();
      return null;
    }
  
    // Abstract methods.
    // -------------------------------------------------------------------
  
    /**
 297:    * Returns the cipher name negotiated for this connection.
 298:    *
 299:    * @return The cipher name.
 300:    * @throws IllegalStateException If the connection has not yet been
 301:    *   established.
 302:    */
   public abstract String getCipherSuite();
  
    /**
 306:    * Returns the certificates used on the local side in this
 307:    * connection.
 308:    *
 309:    * @return The local certificates.
 310:    * @throws IllegalStateException If the connection has not yet been
 311:    *  established.
 312:    */
    public abstract Certificate[] getLocalCertificates();
 
   /**
 316:    * Returns the certificates sent by the other party.
 317:    *
 318:    * @return The peer's certificates.
     * @throws IllegalStateException If the connection has not yet been
     *   established.
     * @throws SSLPeerUnverifiedException If the peer could not be
     *   verified.
     */
    public abstract Certificate[] getServerCertificates() throws SSLPeerUnverifiedException;
  }
