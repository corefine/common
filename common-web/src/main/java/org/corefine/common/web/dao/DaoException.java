package org.corefine.common.web.dao;

import org.corefine.common.web.constant.CodeConstant;
import org.corefine.common.web.service.ServiceException;

/**
 * dao异常
 */
public class DaoException extends ServiceException {

    public DaoException() {
        this(CodeConstant.SERVICE_ERROR, "DAO异常");
    }

    public DaoException(String message) {
        this(CodeConstant.SERVICE_ERROR, message);
    }

    public DaoException(int code, String message) {
        super(code, message);
    }
}
