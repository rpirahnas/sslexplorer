package com.sslexplorer.core;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.upload.FormFile;

import com.sslexplorer.vfs.UploadDetails;

public interface UploadHandler {

	public ActionForward performUpload(HttpServletRequest request, HttpServletResponse response, UploadDetails fileUpload, FormFile file) throws IOException, Exception ;
    
    public boolean checkFileToUpload(HttpServletRequest request, HttpServletResponse response, UploadDetails fileUpload, FormFile file) throws IOException, Exception ;

}
