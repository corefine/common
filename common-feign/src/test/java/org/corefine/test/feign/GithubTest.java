//package org.corefine.test.feign;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class GithubTest {
//    @Autowired
//    private GithubClient githubClient;
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    public void test () {
//        logger.info("size {}", githubClient.contributors("fe19880924", "common").size());
//        logger.info("data {}", githubClient.contributors("fe19880924", "common"));
//    }
//
//    public static class Contributor {
//        String login;
//        int contributions;
//
//        public String getLogin() {
//            return login;
//        }
//
//        public void setLogin(String login) {
//            this.login = login;
//        }
//
//        public int getContributions() {
//            return contributions;
//        }
//
//        public void setContributions(int contributions) {
//            this.contributions = contributions;
//        }
//
//        @Override
//        public String toString() {
//            return "{" +
//                    "login='" + login + '\'' +
//                    ", contributions=" + contributions +
//                    '}';
//        }
//    }
//}