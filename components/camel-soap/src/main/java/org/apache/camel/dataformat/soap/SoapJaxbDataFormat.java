begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.soap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|soap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBIntrospector
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
operator|.
name|BeanInvocation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
operator|.
name|JaxbDataFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|soap
operator|.
name|name
operator|.
name|ElementNameStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|soap
operator|.
name|name
operator|.
name|ServiceInterfaceStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|soap
operator|.
name|name
operator|.
name|TypeNameStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|annotations
operator|.
name|Dataformat
import|;
end_import

begin_comment
comment|/**  * Data format supporting SOAP 1.1 and 1.2.  */
end_comment

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"soapjaxb"
argument_list|)
DECL|class|SoapJaxbDataFormat
specifier|public
class|class
name|SoapJaxbDataFormat
extends|extends
name|JaxbDataFormat
block|{
DECL|field|SOAP_UNMARSHALLED_HEADER_LIST
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_UNMARSHALLED_HEADER_LIST
init|=
literal|"org.apache.camel.dataformat.soap.UNMARSHALLED_HEADER_LIST"
decl_stmt|;
DECL|field|adapter
specifier|private
name|SoapDataFormatAdapter
name|adapter
decl_stmt|;
DECL|field|elementNameStrategy
specifier|private
name|ElementNameStrategy
name|elementNameStrategy
decl_stmt|;
DECL|field|elementNameStrategyRef
specifier|private
name|String
name|elementNameStrategyRef
decl_stmt|;
DECL|field|ignoreUnmarshalledHeaders
specifier|private
name|boolean
name|ignoreUnmarshalledHeaders
decl_stmt|;
DECL|field|version
specifier|private
name|String
name|version
decl_stmt|;
comment|/**      * Remember to set the context path when using this constructor      */
DECL|method|SoapJaxbDataFormat ()
specifier|public
name|SoapJaxbDataFormat
parameter_list|()
block|{     }
comment|/**      * Initialize with JAXB context path      */
DECL|method|SoapJaxbDataFormat (String contextPath)
specifier|public
name|SoapJaxbDataFormat
parameter_list|(
name|String
name|contextPath
parameter_list|)
block|{
name|super
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
block|}
comment|/**      * Initialize the data format. The serviceInterface is necessary to      * determine the element name and namespace of the element inside the soap      * body when marshalling      */
DECL|method|SoapJaxbDataFormat (String contextPath, ElementNameStrategy elementNameStrategy)
specifier|public
name|SoapJaxbDataFormat
parameter_list|(
name|String
name|contextPath
parameter_list|,
name|ElementNameStrategy
name|elementNameStrategy
parameter_list|)
block|{
name|this
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
name|this
operator|.
name|elementNameStrategy
operator|=
name|elementNameStrategy
expr_stmt|;
block|}
comment|/**      * Initialize the data format. The serviceInterface is necessary to      * determine the element name and namespace of the element inside the soap      * body when marshalling      */
DECL|method|SoapJaxbDataFormat (String contextPath, String elementNameStrategyRef)
specifier|public
name|SoapJaxbDataFormat
parameter_list|(
name|String
name|contextPath
parameter_list|,
name|String
name|elementNameStrategyRef
parameter_list|)
block|{
name|this
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
name|this
operator|.
name|elementNameStrategyRef
operator|=
name|elementNameStrategyRef
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"soapjaxb"
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
literal|"1.2"
operator|.
name|equals
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using SOAP 1.2 adapter"
argument_list|)
expr_stmt|;
name|adapter
operator|=
operator|new
name|Soap12DataFormatAdapter
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using SOAP 1.1 adapter"
argument_list|)
expr_stmt|;
name|adapter
operator|=
operator|new
name|Soap11DataFormatAdapter
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
DECL|method|checkElementNameStrategy (Exchange exchange)
specifier|protected
name|void
name|checkElementNameStrategy
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|elementNameStrategy
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|elementNameStrategy
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
else|else
block|{
if|if
condition|(
name|elementNameStrategyRef
operator|!=
literal|null
condition|)
block|{
name|elementNameStrategy
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|elementNameStrategyRef
argument_list|,
name|ElementNameStrategy
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|elementNameStrategy
operator|=
operator|new
name|TypeNameStrategy
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/**      * Marshal inputObjects to SOAP xml. If the exchange or message has an      * EXCEPTION_CAUGTH property or header then instead of the object the      * exception is marshaled.      *       * To determine the name of the top level xml elements the elementNameStrategy      * is used.      */
DECL|method|marshal (Exchange exchange, Object inputObject, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|inputObject
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|checkElementNameStrategy
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|String
name|soapAction
init|=
name|getSoapActionFromExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|soapAction
operator|==
literal|null
operator|&&
name|inputObject
operator|instanceof
name|BeanInvocation
condition|)
block|{
name|BeanInvocation
name|beanInvocation
init|=
operator|(
name|BeanInvocation
operator|)
name|inputObject
decl_stmt|;
name|WebMethod
name|webMethod
init|=
name|beanInvocation
operator|.
name|getMethod
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|WebMethod
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|webMethod
operator|!=
literal|null
operator|&&
name|webMethod
operator|.
name|action
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|soapAction
operator|=
name|webMethod
operator|.
name|action
argument_list|()
expr_stmt|;
block|}
block|}
name|Object
name|envelope
init|=
name|adapter
operator|.
name|doMarshal
argument_list|(
name|exchange
argument_list|,
name|inputObject
argument_list|,
name|stream
argument_list|,
name|soapAction
argument_list|)
decl_stmt|;
comment|// and continue in super
name|super
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|,
name|envelope
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create body content from a non Exception object. If the inputObject is a      * BeanInvocation the following should be considered: The first parameter      * will be used for the SOAP body. BeanInvocations with more than one      * parameter are not supported. So the interface should be in doc lit bare      * style.      *       * @param inputObject      *            object to be put into the SOAP body      * @param soapAction      *            for name resolution      * @param headerElements      *            in/out parameter used to capture header content if present      *                  * @return JAXBElement for the body content      */
DECL|method|createContentFromObject (final Object inputObject, String soapAction, List<Object> headerElements)
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|createContentFromObject
parameter_list|(
specifier|final
name|Object
name|inputObject
parameter_list|,
name|String
name|soapAction
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|headerElements
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|bodyParts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|headerParts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|inputObject
operator|instanceof
name|BeanInvocation
condition|)
block|{
name|BeanInvocation
name|bi
init|=
operator|(
name|BeanInvocation
operator|)
name|inputObject
decl_stmt|;
name|Annotation
index|[]
index|[]
name|annotations
init|=
name|bi
operator|.
name|getMethod
argument_list|()
operator|.
name|getParameterAnnotations
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|WebParam
argument_list|>
name|webParams
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Annotation
index|[]
name|singleParameterAnnotations
range|:
name|annotations
control|)
block|{
for|for
control|(
name|Annotation
name|annotation
range|:
name|singleParameterAnnotations
control|)
block|{
if|if
condition|(
name|annotation
operator|instanceof
name|WebParam
condition|)
block|{
name|webParams
operator|.
name|add
argument_list|(
operator|(
name|WebParam
operator|)
name|annotation
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|webParams
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|webParams
operator|.
name|size
argument_list|()
operator|==
name|bi
operator|.
name|getArgs
argument_list|()
operator|.
name|length
condition|)
block|{
name|int
name|index
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|bi
operator|.
name|getArgs
argument_list|()
control|)
block|{
if|if
condition|(
name|webParams
operator|.
name|get
argument_list|(
operator|++
name|index
argument_list|)
operator|.
name|header
argument_list|()
condition|)
block|{
name|headerParts
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bodyParts
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"The number of bean invocation parameters does not "
operator|+
literal|"match the number of parameters annotated with @WebParam for the method [ "
operator|+
name|bi
operator|.
name|getMethod
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"]."
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// try to map all objects for the body
for|for
control|(
name|Object
name|o
range|:
name|bi
operator|.
name|getArgs
argument_list|()
control|)
block|{
name|bodyParts
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|bodyParts
operator|.
name|add
argument_list|(
name|inputObject
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Object
argument_list|>
name|bodyElements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|bodyObj
range|:
name|bodyParts
control|)
block|{
name|QName
name|name
init|=
name|elementNameStrategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
name|soapAction
argument_list|,
name|bodyObj
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Could not find QName for class {}"
argument_list|,
name|bodyObj
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
else|else
block|{
name|bodyElements
operator|.
name|add
argument_list|(
name|getElement
argument_list|(
name|bodyObj
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Object
name|headerObj
range|:
name|headerParts
control|)
block|{
name|QName
name|name
init|=
name|elementNameStrategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
name|soapAction
argument_list|,
name|headerObj
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Could not find QName for class {}"
argument_list|,
name|headerObj
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
else|else
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|headerElem
init|=
name|getElement
argument_list|(
name|headerObj
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|headerElem
condition|)
block|{
name|headerElements
operator|.
name|add
argument_list|(
name|headerElem
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|bodyElements
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|getElement (Object fromObj, QName name)
specifier|private
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|getElement
parameter_list|(
name|Object
name|fromObj
parameter_list|,
name|QName
name|name
parameter_list|)
block|{
name|Object
name|value
init|=
literal|null
decl_stmt|;
comment|// In the case of a parameter, the class of the value of the holder class
comment|// is used for the mapping rather than the holder class itself.
if|if
condition|(
name|fromObj
operator|instanceof
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
condition|)
block|{
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
name|holder
init|=
operator|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
operator|)
name|fromObj
decl_stmt|;
name|value
operator|=
name|holder
operator|.
name|value
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|value
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
name|value
operator|=
name|fromObj
expr_stmt|;
block|}
return|return
operator|new
name|JAXBElement
argument_list|(
name|name
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Unmarshal a given SOAP xml stream and return the content of the SOAP body      */
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|checkElementNameStrategy
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|String
name|soapAction
init|=
name|getSoapActionFromExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// Determine the method name for an eventual BeanProcessor in the route
if|if
condition|(
name|soapAction
operator|!=
literal|null
operator|&&
name|elementNameStrategy
operator|instanceof
name|ServiceInterfaceStrategy
condition|)
block|{
name|ServiceInterfaceStrategy
name|strategy
init|=
operator|(
name|ServiceInterfaceStrategy
operator|)
name|elementNameStrategy
decl_stmt|;
name|String
name|methodName
init|=
name|strategy
operator|.
name|getMethodForSoapAction
argument_list|(
name|soapAction
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|BEAN_METHOD_NAME
argument_list|,
name|methodName
argument_list|)
expr_stmt|;
block|}
comment|// Store soap action for an eventual later marshal step.
comment|// This is necessary as the soap action in the message may get lost on the way
if|if
condition|(
name|soapAction
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SOAP_ACTION
argument_list|,
name|soapAction
argument_list|)
expr_stmt|;
block|}
name|Object
name|unmarshalledObject
init|=
name|super
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
name|stream
argument_list|)
decl_stmt|;
name|Object
name|rootObject
init|=
name|JAXBIntrospector
operator|.
name|getValue
argument_list|(
name|unmarshalledObject
argument_list|)
decl_stmt|;
return|return
name|adapter
operator|.
name|doUnmarshal
argument_list|(
name|exchange
argument_list|,
name|stream
argument_list|,
name|rootObject
argument_list|)
return|;
block|}
DECL|method|getSoapActionFromExchange (Exchange exchange)
specifier|private
name|String
name|getSoapActionFromExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|soapAction
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|SOAP_ACTION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|soapAction
operator|==
literal|null
condition|)
block|{
name|soapAction
operator|=
name|inMessage
operator|.
name|getHeader
argument_list|(
literal|"SOAPAction"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|soapAction
operator|!=
literal|null
operator|&&
name|soapAction
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|soapAction
operator|=
name|soapAction
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|soapAction
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|soapAction
operator|==
literal|null
condition|)
block|{
name|soapAction
operator|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|SOAP_ACTION
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|soapAction
return|;
block|}
comment|/**      * Added the generated SOAP package to the JAXB context so Soap datatypes      * are available      */
annotation|@
name|Override
DECL|method|createContext ()
specifier|protected
name|JAXBContext
name|createContext
parameter_list|()
throws|throws
name|JAXBException
block|{
if|if
condition|(
name|getContextPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|adapter
operator|.
name|getSoapPackageName
argument_list|()
operator|+
literal|":"
operator|+
name|getContextPath
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|JAXBContext
operator|.
name|newInstance
argument_list|()
return|;
block|}
block|}
DECL|method|getElementNameStrategy ()
specifier|public
name|ElementNameStrategy
name|getElementNameStrategy
parameter_list|()
block|{
return|return
name|elementNameStrategy
return|;
block|}
DECL|method|setElementNameStrategy (Object nameStrategy)
specifier|public
name|void
name|setElementNameStrategy
parameter_list|(
name|Object
name|nameStrategy
parameter_list|)
block|{
if|if
condition|(
name|nameStrategy
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|elementNameStrategy
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nameStrategy
operator|instanceof
name|ElementNameStrategy
condition|)
block|{
name|this
operator|.
name|elementNameStrategy
operator|=
operator|(
name|ElementNameStrategy
operator|)
name|nameStrategy
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The argument for setElementNameStrategy should be subClass of "
operator|+
name|ElementNameStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|getElementNameStrategyRef ()
specifier|public
name|String
name|getElementNameStrategyRef
parameter_list|()
block|{
return|return
name|elementNameStrategyRef
return|;
block|}
DECL|method|setElementNameStrategyRef (String elementNameStrategyRef)
specifier|public
name|void
name|setElementNameStrategyRef
parameter_list|(
name|String
name|elementNameStrategyRef
parameter_list|)
block|{
name|this
operator|.
name|elementNameStrategyRef
operator|=
name|elementNameStrategyRef
expr_stmt|;
block|}
DECL|method|isIgnoreUnmarshalledHeaders ()
specifier|public
name|boolean
name|isIgnoreUnmarshalledHeaders
parameter_list|()
block|{
return|return
name|ignoreUnmarshalledHeaders
return|;
block|}
DECL|method|setIgnoreUnmarshalledHeaders (boolean ignoreUnmarshalledHeaders)
specifier|public
name|void
name|setIgnoreUnmarshalledHeaders
parameter_list|(
name|boolean
name|ignoreUnmarshalledHeaders
parameter_list|)
block|{
name|this
operator|.
name|ignoreUnmarshalledHeaders
operator|=
name|ignoreUnmarshalledHeaders
expr_stmt|;
block|}
DECL|method|getVersion ()
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
DECL|method|setVersion (String version)
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
block|}
end_class

end_unit

