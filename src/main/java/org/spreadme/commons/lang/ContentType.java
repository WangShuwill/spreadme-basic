/*
 *    Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License"),
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.spreadme.commons.lang;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shuwei.wang
 * @since 1.0.0
 */
public enum ContentType {

	txt("text/plain"),
	css("text/css"),
	html("text/html"),
	htm("text/html"),
	gif("image/gif"),
	jpg("image/jpeg"),
	jpe("image/jpeg"),
	jpeg("image/jpeg"),
	bmp("image/bmp"),
	js("application/javascript"),
	png("image/png"),
	java("text/plain"),
	body("text/html"),
	rtx("text/richtext"),
	tsv("text/tab-separated-values"),
	etx("text/x-setext"),
	json("application/json"),
	csh("application/x-csh"),
	sh("application/x-sh"),
	tcl("application/x-tcl"),
	tex("application/x-tex"),
	texinfo("application/x-texinfo"),
	texi("application/x-texinfo"),
	t("application/x-troff"),
	tr("application/x-troff"),
	roff("application/x-troff"),
	man("application/x-troff-man"),
	me("application/x-troff-me"),
	ms("application/x-wais-source"),
	src("application/x-wais-source"),
	zip("application/zip"),
	bcpio("application/x-bcpio"),
	cpio("application/x-cpio"),
	gtar("application/x-gtar"),
	shar("application/x-shar"),
	sv4cpio("application/x-sv4cpio"),
	sv4crc("application/x-sv4crc"),
	tar("application/x-tar"),
	ustar("application/x-ustar"),
	dvi("application/x-dvi"),
	hdf("application/x-hdf"),
	latex("application/x-latex"),
	bin("application/octet-stream"),
	oda("application/oda"),
	pdf("application/pdf"),
	ps("application/postscript"),
	eps("application/postscript"),
	ai("application/postscript"),
	rtf("application/rtf"),
	nc("application/x-netcdf"),
	cdf("application/x-netcdf"),
	cer("application/x-x509-ca-cert"),
	exe("application/octet-stream"),
	gz("application/x-gzip"),
	Z("application/x-compress"),
	z("application/x-compress"),
	hqx("application/mac-binhex40"),
	mif("application/x-mif"),
	ico("image/x-icon"),
	ief("image/ief"),
	tiff("image/tiff"),
	tif("image/tiff"),
	ras("image/x-cmu-raster"),
	pnm("image/x-portable-anymap"),
	pbm("image/x-portable-bitmap"),
	pgm("image/x-portable-graymap"),
	ppm("image/x-portable-pixmap"),
	rgb("image/x-rgb"),
	xbm("image/x-xbitmap"),
	xpm("image/x-xpixmap"),
	xwd("image/x-xwindowdump"),
	au("audio/basic"),
	snd("audio/basic"),
	aif("audio/x-aiff"),
	aiff("audio/x-aiff"),
	aifc("audio/x-aiff"),
	wav("audio/x-wav"),
	mp3("audio/mpeg"),
	mpeg("video/mpeg"),
	mpg("video/mpeg"),
	mpe("video/mpeg"),
	qt("video/quicktime"),
	mov("video/quicktime"),
	avi("video/x-msvideo"),
	movie("video/x-sgi-movie"),
	avx("video/x-rad-screenplay"),
	wrl("x-world/x-vrml"),
	mpv2("video/mpeg2"),
	jnlp("application/x-java-jnlp-file"),

	eot("application/vnd.ms-fontobject"),
	woff("application/font-woff"),
	woff2("application/font-woff2"),
	ttf("application/x-font-ttf"),
	otf("application/x-font-opentype"),
	sfnt("application/font-sfnt"),

	/* Add XML related MIMEs */
	xml("application/xml"),
	xhtml("application/xhtml+xml"),
	xsl("application/xml"),
	svg("image/svg+xml"),
	svgz("image/svg+xml"),
	wbmp("image/vnd.wap.wbmp"),
	wml("text/vnd.wap.wml"),
	wmlc("application/vnd.wap.wmlc"),
	wmls("text/vnd.wap.wmlscript"),
	wmlscriptc("application/vnd.wap.wmlscriptc"),

	/* office */
	xlsx("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	ppts("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
	docx("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
	doc("application/msword"),
	wps("application/msword"),
	xls("application/vnd.ms-excel"),
	ppt("application/vnd.ms-powerpoint"),

	multipart("multipart/form-data"),
	formurlencoded("application/x-www-form-urlencoded");

	private static Map<String, ContentType> map = new HashMap<>(132);

	static {
		for (ContentType mimeType : ContentType.values()) {
			map.put(mimeType.name(), mimeType);
		}
	}

	private String value;

	ContentType(String value) {
		this.value = value;
	}

	public String getType() {
		return value;
	}

}
