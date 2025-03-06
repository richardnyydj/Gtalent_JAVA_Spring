package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SitemapController {

    @GetMapping(value = "/sitemap.xml", produces = "application/xml")
    public String getSitemap() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">"
                + "<url><loc>https://gtalent.ddns.net/</loc><changefreq>daily</changefreq><priority>1.0</priority></url>"
                + "</urlset>";
    }

    // 給搜尋引擎看的 robots.txt，使網站在搜尋引擎中有更好的ranking
    @GetMapping(value = "/robots.txt", produces = "text/plain")
    public String getRobotsTxt() {
        StringBuilder sb = new StringBuilder();
        sb.append("User-agent: *\n");
        sb.append("Disallow: /admin/\n");
        sb.append("Allow: /\n");
        sb.append("Sitemap: https://gtalent.ddns.net/sitemap.xml\n");
        return sb.toString();
    }
}

