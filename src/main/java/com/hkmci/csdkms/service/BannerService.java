package com.hkmci.csdkms.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.entity.Banner;
import com.hkmci.csdkms.model.BannerModel;

public interface BannerService {
	public  Banner save (Banner TheModel);
	public List<Banner> findAll(Integer level);
	public List<Banner> findTop(Integer access_channel);
	public List<Banner> findGeneral(Integer access_channel);
	public Optional<Banner> findById(Long Id);

	public Path del(int Id);
	void deleteBanner(Long bannerId, Long userId);

}
