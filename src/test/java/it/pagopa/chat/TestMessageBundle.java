/**
 * 
 */
package it.pagopa.chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.MissingResourceException;

import org.junit.Before;
import org.junit.Test;

import it.pagopa.chat.util.PagoPAChatResourceBundle;

/**
 * @author VACCARO
 *
 */
public class TestMessageBundle {
	
	private PagoPAChatResourceBundle cut;
	
	private final static String PARAM_1 = "param1";
	private final static String PARAM_2 = "param2";
	
	private final static String SEPARATOR = " - ";
	
	@Before
	public void init() {
		this.cut = PagoPAChatResourceBundle.getInstance();
	}

	@Test
    public void testGetInstance() {
		PagoPAChatResourceBundle toCompare = PagoPAChatResourceBundle.getInstance();
		
		assertNotNull(this.cut);
		assertSame("Il metodo MessageBundle.getInstance() ha restituito due oggetti diversi dopo due invocazioni", this.cut, toCompare);
    }
	
	@Test
    public void testGetMessageBase() {
		String message = cut.getString("bundle.test.base");
		
		assertNotNull(message);
		assertEquals(message, "test");
		
	}
	
	@Test
    public void testGetMessageSingleParameter() {
		String message = cut.getString("bundle.test.un.parametro", PARAM_1);
		
		assertNotNull(message);
		assertEquals(PARAM_1, message);
    }
	
	@Test
    public void testGetMessageDoubleParameter() {
		String message = cut.getString("bundle.test.due.parametri", PARAM_1, PARAM_2);
		
		assertNotNull(message);
		assertEquals(PARAM_1 + SEPARATOR + PARAM_2, message);
    }
	
	@Test(expected=MissingResourceException.class)
    public void testGetMessageWrongKey() {
		cut.getString("wrong.key");
    }
	
	@Test(expected=NullPointerException.class)
    public void testGetMessageNullKey() {
		cut.getString(null);
    }
	
	
}
