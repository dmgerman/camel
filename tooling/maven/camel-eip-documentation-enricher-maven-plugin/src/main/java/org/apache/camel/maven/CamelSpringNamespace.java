begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|NamespaceContext
import|;
end_import

begin_comment
comment|/**  * Default namespace for xsd schema.  */
end_comment

begin_class
DECL|class|CamelSpringNamespace
specifier|public
class|class
name|CamelSpringNamespace
implements|implements
name|NamespaceContext
block|{
annotation|@
name|Override
DECL|method|getNamespaceURI (String prefix)
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The prefix cannot be null."
argument_list|)
throw|;
block|}
if|if
condition|(
name|Constants
operator|.
name|XML_SCHEMA_NAMESPACE_PREFIX
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
return|return
name|Constants
operator|.
name|XML_SCHEMA_NAMESPACE_URI
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getPrefix (String namespaceURI)
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Operation not supported"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|getPrefixes (String namespaceURI)
specifier|public
name|Iterator
name|getPrefixes
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Operation not supported"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

