package ch.erik.ebooktranslator.service;

import org.jdom2.JDOMException;

import java.io.IOException;

public interface XmlFragmentProcessor {

    byte[] processHtmlFragment(final byte[] xmlFragment) throws JDOMException, IOException;

}
