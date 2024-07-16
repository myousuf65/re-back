package com.hkmci.csdkms.service;

import java.util.List;

import com.hkmci.csdkms.entity.Faq;
import com.hkmci.csdkms.model.FaqReturn;

public interface FAQService {

	List<FaqReturn> RetrunFaq();

	String CreateFaq(FaqReturn createFaq, Long userId);

	List<FaqReturn> adminParentFaq();

	List<FaqReturn> adminGetChild(Long parentId, List<Faq> all_data);

	List<FaqReturn> getFaq(Long faqId);

	void deleteFaq(Long faqId, Long userId);

	FaqReturn adminUpdateFaq(FaqReturn update_data, Long userId);

}
