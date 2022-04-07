package com.sddzinfo.html2docx.repository;

import com.sddzinfo.html2docx.entity.BzyNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BzyNewsRepository extends JpaRepository<BzyNews, Integer>, JpaSpecificationExecutor<BzyNews> {

}