begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.soap.name
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
operator|.
name|name
package|;
end_package

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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|jws
operator|.
name|WebResult
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|RequestWrapper
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|ResponseWrapper
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebFault
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Offers a finder for a webservice interface to determine the QName of a  * webservice data element  */
end_comment

begin_class
DECL|class|ServiceInterfaceStrategy
specifier|public
class|class
name|ServiceInterfaceStrategy
implements|implements
name|ElementNameStrategy
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ServiceInterfaceStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|soapActionToMethodInfo
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|MethodInfo
argument_list|>
name|soapActionToMethodInfo
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|MethodInfo
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|inTypeNameToQName
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|QName
argument_list|>
name|inTypeNameToQName
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|QName
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|outTypeNameToQName
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|QName
argument_list|>
name|outTypeNameToQName
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|QName
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|isClient
specifier|private
name|boolean
name|isClient
decl_stmt|;
DECL|field|fallBackStrategy
specifier|private
name|ElementNameStrategy
name|fallBackStrategy
decl_stmt|;
DECL|field|faultNameToException
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
argument_list|>
name|faultNameToException
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Init with JAX-WS service interface      *       * @param serviceInterface      * @param isClient      *            determines if marhalling looks at input or output of method      */
DECL|method|ServiceInterfaceStrategy (Class<?> serviceInterface, boolean isClient)
specifier|public
name|ServiceInterfaceStrategy
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|serviceInterface
parameter_list|,
name|boolean
name|isClient
parameter_list|)
block|{
name|analyzeServiceInterface
argument_list|(
name|serviceInterface
argument_list|)
expr_stmt|;
name|this
operator|.
name|isClient
operator|=
name|isClient
expr_stmt|;
name|this
operator|.
name|fallBackStrategy
operator|=
operator|new
name|TypeNameStrategy
argument_list|()
expr_stmt|;
block|}
DECL|method|getMethodForSoapAction (String soapAction)
specifier|public
name|String
name|getMethodForSoapAction
parameter_list|(
name|String
name|soapAction
parameter_list|)
block|{
name|MethodInfo
name|methodInfo
init|=
name|soapActionToMethodInfo
operator|.
name|get
argument_list|(
name|soapAction
argument_list|)
decl_stmt|;
return|return
operator|(
name|methodInfo
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|methodInfo
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|method|getOutInfo (Method method)
specifier|private
name|TypeInfo
name|getOutInfo
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|ResponseWrapper
name|respWrap
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|ResponseWrapper
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|respWrap
operator|!=
literal|null
operator|&&
name|respWrap
operator|.
name|className
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|TypeInfo
argument_list|(
name|respWrap
operator|.
name|className
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
name|respWrap
operator|.
name|targetNamespace
argument_list|()
argument_list|,
name|respWrap
operator|.
name|localName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|returnType
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
if|if
condition|(
name|Void
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|returnType
argument_list|)
condition|)
block|{
return|return
operator|new
name|TypeInfo
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
else|else
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
name|WebResult
name|webResult
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|WebResult
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|webResult
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|TypeInfo
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
name|webResult
operator|.
name|targetNamespace
argument_list|()
argument_list|,
name|webResult
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Result type of method "
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|" is not annotated with WebParam. This is not yet supported"
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|getInInfo (Method method)
specifier|private
name|List
argument_list|<
name|TypeInfo
argument_list|>
name|getInInfo
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|List
argument_list|<
name|TypeInfo
argument_list|>
name|typeInfos
init|=
operator|new
name|ArrayList
argument_list|<
name|TypeInfo
argument_list|>
argument_list|()
decl_stmt|;
name|RequestWrapper
name|requestWrapper
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|RequestWrapper
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// parameter types are returned in declaration order
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|types
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|types
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|typeInfos
operator|.
name|add
argument_list|(
operator|new
name|TypeInfo
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|typeInfos
return|;
block|}
if|if
condition|(
name|requestWrapper
operator|!=
literal|null
operator|&&
name|requestWrapper
operator|.
name|className
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|typeInfos
operator|.
name|add
argument_list|(
operator|new
name|TypeInfo
argument_list|(
name|requestWrapper
operator|.
name|className
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
name|requestWrapper
operator|.
name|targetNamespace
argument_list|()
argument_list|,
name|requestWrapper
operator|.
name|localName
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|typeInfos
return|;
block|}
comment|// annotations are returned in declaration order
name|Annotation
index|[]
index|[]
name|annotations
init|=
name|method
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
argument_list|<
name|WebParam
argument_list|>
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
operator|!=
name|types
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The number of @WebParam annotations for Method "
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|" does not match the number of parameters. This is not supported."
argument_list|)
throw|;
block|}
name|Iterator
argument_list|<
name|WebParam
argument_list|>
name|webParamIter
init|=
name|webParams
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|int
name|paramCounter
init|=
operator|-
literal|1
decl_stmt|;
while|while
condition|(
name|webParamIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|WebParam
name|webParam
init|=
name|webParamIter
operator|.
name|next
argument_list|()
decl_stmt|;
name|typeInfos
operator|.
name|add
argument_list|(
operator|new
name|TypeInfo
argument_list|(
name|types
index|[
operator|++
name|paramCounter
index|]
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
name|webParam
operator|.
name|targetNamespace
argument_list|()
argument_list|,
name|webParam
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|typeInfos
return|;
block|}
comment|/**      * Determines how the parameter object of the service method will be named      * in xml. It will use either the RequestWrapper annotation of the method if      * present or the WebParam method of the parameter.      *       * @param method      */
DECL|method|analyzeMethod (Method method)
specifier|private
name|MethodInfo
name|analyzeMethod
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|List
argument_list|<
name|TypeInfo
argument_list|>
name|inInfos
init|=
name|getInInfo
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|TypeInfo
name|outInfo
init|=
name|getOutInfo
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|WebMethod
name|webMethod
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|WebMethod
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|soapAction
init|=
operator|(
name|webMethod
operator|!=
literal|null
operator|)
condition|?
name|webMethod
operator|.
name|action
argument_list|()
else|:
literal|null
decl_stmt|;
return|return
operator|new
name|MethodInfo
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|soapAction
argument_list|,
name|inInfos
operator|.
name|toArray
argument_list|(
operator|new
name|TypeInfo
index|[
name|inInfos
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|outInfo
argument_list|)
return|;
block|}
DECL|method|analyzeServiceInterface (Class<?> serviceInterface)
specifier|private
name|void
name|analyzeServiceInterface
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|serviceInterface
parameter_list|)
block|{
name|Method
index|[]
name|methods
init|=
name|serviceInterface
operator|.
name|getMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
name|MethodInfo
name|info
init|=
name|analyzeMethod
argument_list|(
name|method
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|info
operator|.
name|getIn
argument_list|()
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|TypeInfo
name|ti
init|=
name|info
operator|.
name|getIn
argument_list|()
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|inTypeNameToQName
operator|.
name|containsKey
argument_list|(
name|ti
operator|.
name|getTypeName
argument_list|()
argument_list|)
operator|&&
operator|(
operator|!
operator|(
name|ti
operator|.
name|getTypeName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"javax.xml.ws.Holder"
argument_list|)
operator|)
operator|)
operator|&&
operator|(
operator|!
operator|(
name|inTypeNameToQName
operator|.
name|get
argument_list|(
name|ti
operator|.
name|getTypeName
argument_list|()
argument_list|)
operator|.
name|equals
argument_list|(
name|ti
operator|.
name|getElName
argument_list|()
argument_list|)
operator|)
operator|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ambiguous QName mapping. The type [ "
operator|+
name|ti
operator|.
name|getTypeName
argument_list|()
operator|+
literal|" ] is already mapped to a QName in this context."
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|inTypeNameToQName
operator|.
name|put
argument_list|(
name|ti
operator|.
name|getTypeName
argument_list|()
argument_list|,
name|ti
operator|.
name|getElName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|info
operator|.
name|getSoapAction
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|info
operator|.
name|getSoapAction
argument_list|()
argument_list|)
condition|)
block|{
name|soapActionToMethodInfo
operator|.
name|put
argument_list|(
name|info
operator|.
name|getSoapAction
argument_list|()
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
name|outTypeNameToQName
operator|.
name|put
argument_list|(
name|info
operator|.
name|getOut
argument_list|()
operator|.
name|getTypeName
argument_list|()
argument_list|,
name|info
operator|.
name|getOut
argument_list|()
operator|.
name|getElName
argument_list|()
argument_list|)
expr_stmt|;
name|addExceptions
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|addExceptions (Method method)
specifier|private
name|void
name|addExceptions
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|exTypes
init|=
name|method
operator|.
name|getExceptionTypes
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|exType
range|:
name|exTypes
control|)
block|{
name|WebFault
name|webFault
init|=
name|exType
operator|.
name|getAnnotation
argument_list|(
name|WebFault
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|webFault
operator|!=
literal|null
condition|)
block|{
name|QName
name|faultName
init|=
operator|new
name|QName
argument_list|(
name|webFault
operator|.
name|targetNamespace
argument_list|()
argument_list|,
name|webFault
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|faultNameToException
operator|.
name|put
argument_list|(
name|faultName
argument_list|,
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
operator|)
name|exType
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Determine the QName of the method parameter of the method that matches      * either soapAction and type or if not possible only the type      *       * @param soapAction      * @param type      * @return matching QName throws RuntimeException if no matching QName was      *         found      */
DECL|method|findQNameForSoapActionOrType (String soapAction, Class<?> type)
specifier|public
name|QName
name|findQNameForSoapActionOrType
parameter_list|(
name|String
name|soapAction
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|MethodInfo
name|info
init|=
name|soapActionToMethodInfo
operator|.
name|get
argument_list|(
name|soapAction
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|isClient
condition|)
block|{
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
name|info
operator|.
name|getIn
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|getElName
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
return|return
name|info
operator|.
name|getOut
argument_list|()
operator|.
name|getElName
argument_list|()
return|;
block|}
block|}
name|QName
name|qName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|isClient
condition|)
block|{
name|qName
operator|=
name|inTypeNameToQName
operator|.
name|get
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|qName
operator|=
name|outTypeNameToQName
operator|.
name|get
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|qName
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|qName
operator|=
name|fallBackStrategy
operator|.
name|findQNameForSoapActionOrType
argument_list|(
name|soapAction
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"No method found that matches the given SoapAction "
operator|+
name|soapAction
operator|+
literal|" or that has an "
operator|+
operator|(
name|isClient
condition|?
literal|"input"
else|:
literal|"output"
operator|)
operator|+
literal|" of type "
operator|+
name|type
operator|.
name|getName
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|qName
return|;
block|}
DECL|method|findExceptionForFaultName (QName faultName)
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
name|findExceptionForFaultName
parameter_list|(
name|QName
name|faultName
parameter_list|)
block|{
return|return
name|faultNameToException
operator|.
name|get
argument_list|(
name|faultName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

