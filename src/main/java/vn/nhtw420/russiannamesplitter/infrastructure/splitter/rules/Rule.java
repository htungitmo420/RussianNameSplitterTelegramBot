package vn.nhtw420.russiannamesplitter.infrastructure.splitter.rules;

import vn.nhtw420.russiannamesplitter.infrastructure.splitter.support.SplitContext;

public interface Rule {
    void apply(SplitContext context);
}
