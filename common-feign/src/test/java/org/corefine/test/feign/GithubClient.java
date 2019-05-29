//package org.corefine.test.feign;
//
//import org.corefine.common.feign.FeignClient;
//
//import java.util.List;
//
//import feign.Param;
//import feign.RequestLine;
//
//@FeignClient("github")
//public interface GithubClient {
//    @RequestLine("GET /repos/{owner}/{repo}/contributors")
//    List<GithubTest.Contributor> contributors(@Param("owner") String owner, @Param("repo") String repo);
//}
