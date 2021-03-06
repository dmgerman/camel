begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|annotation
operator|.
name|XmlSchema
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
name|annotation
operator|.
name|XmlType
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|XmlRootElementPreferringElementNameStrategy
specifier|public
class|class
name|XmlRootElementPreferringElementNameStrategy
implements|implements
name|ElementNameStrategy
block|{
DECL|field|DEFAULT_NS
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_NS
init|=
literal|"##default"
decl_stmt|;
annotation|@
name|Override
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
name|XmlType
name|xmlType
init|=
name|type
operator|.
name|getAnnotation
argument_list|(
name|XmlType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xmlType
operator|==
literal|null
operator|||
name|xmlType
operator|.
name|name
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The type "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" needs to have an XmlType annotation with name"
argument_list|)
throw|;
block|}
comment|// prefer name+ns from the XmlRootElement, and fallback to XmlType
name|String
name|localName
init|=
literal|null
decl_stmt|;
name|String
name|nameSpace
init|=
literal|null
decl_stmt|;
name|XmlRootElement
name|root
init|=
name|type
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|localName
operator|=
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|localName
argument_list|)
condition|?
name|root
operator|.
name|name
argument_list|()
else|:
name|localName
expr_stmt|;
name|nameSpace
operator|=
name|isInValidNamespace
argument_list|(
name|nameSpace
argument_list|)
condition|?
name|root
operator|.
name|namespace
argument_list|()
else|:
name|nameSpace
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|localName
operator|=
name|xmlType
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|isInValidNamespace
argument_list|(
name|nameSpace
argument_list|)
condition|)
block|{
name|XmlSchema
name|xmlSchema
init|=
name|type
operator|.
name|getPackage
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|XmlSchema
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xmlSchema
operator|!=
literal|null
condition|)
block|{
name|nameSpace
operator|=
name|xmlSchema
operator|.
name|namespace
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|isInValidNamespace
argument_list|(
name|nameSpace
argument_list|)
condition|)
block|{
name|nameSpace
operator|=
name|xmlType
operator|.
name|namespace
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|localName
argument_list|)
operator|||
name|isInValidNamespace
argument_list|(
name|nameSpace
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to determine localName or namespace for type<"
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|">"
argument_list|)
throw|;
block|}
return|return
operator|new
name|QName
argument_list|(
name|nameSpace
argument_list|,
name|localName
argument_list|)
return|;
block|}
DECL|method|isInValidNamespace (String namespace)
specifier|private
name|boolean
name|isInValidNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|namespace
argument_list|)
operator|||
name|DEFAULT_NS
operator|.
name|equalsIgnoreCase
argument_list|(
name|namespace
argument_list|)
return|;
block|}
annotation|@
name|Override
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Exception lookup is not supported"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

