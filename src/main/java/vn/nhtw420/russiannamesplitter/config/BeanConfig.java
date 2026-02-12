package vn.nhtw420.russiannamesplitter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.nhtw420.russiannamesplitter.application.port.FioSplitterPort;
import vn.nhtw420.russiannamesplitter.application.port.ShortNamePort;
import vn.nhtw420.russiannamesplitter.application.port.TextNormalizerPort;
import vn.nhtw420.russiannamesplitter.application.usecase.SplitFioUseCase;
import vn.nhtw420.russiannamesplitter.application.usecase.SuggestShortNameUseCase;

@Configuration
public class BeanConfig {

    @Bean
    public SplitFioUseCase splitFioUseCase(
            TextNormalizerPort normalizer,
            FioSplitterPort splitter,
            ShortNamePort shortNamePort
    ) {
        return new SplitFioUseCase(normalizer, splitter, shortNamePort);
    }

    @Bean
    public SuggestShortNameUseCase suggestShortNameUseCase(ShortNamePort shortNamePort) {
        return new SuggestShortNameUseCase(shortNamePort);
    }
}
