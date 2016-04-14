package org.gooru.nucleus.converter.processors.command.executor.convert;

import org.gooru.nucleus.converter.processors.command.executor.Executor;

public final class ConvertExecutorFactory {

    public static Executor ConvertHtmlToPdfExecutor() {
        return ConvertHtmlToPdfExecutor.getInstance();
    }

    public static Executor ConvertHtmlToExcelExecutor() {
        return ConvertHtmlToExcelExecutor.getInstance();
    }

    private ConvertExecutorFactory() {
        throw new AssertionError();
    }

}
