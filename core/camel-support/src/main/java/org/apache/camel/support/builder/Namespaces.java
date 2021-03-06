begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|builder
package|;
end_package

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
name|Map
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
name|NamespaceAware
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

begin_comment
comment|/**  * A helper class for working with namespaces.  */
end_comment

begin_class
DECL|class|Namespaces
specifier|public
class|class
name|Namespaces
block|{
DECL|field|DEFAULT_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_NAMESPACE
init|=
literal|"http://camel.apache.org/schema/spring"
decl_stmt|;
DECL|field|IN_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|IN_NAMESPACE
init|=
literal|"http://camel.apache.org/xml/in/"
decl_stmt|;
DECL|field|OUT_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|OUT_NAMESPACE
init|=
literal|"http://camel.apache.org/xml/out/"
decl_stmt|;
DECL|field|FUNCTION_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|FUNCTION_NAMESPACE
init|=
literal|"http://camel.apache.org/xml/function/"
decl_stmt|;
DECL|field|SYSTEM_PROPERTIES_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|SYSTEM_PROPERTIES_NAMESPACE
init|=
literal|"http://camel.apache.org/xml/variables/system-properties"
decl_stmt|;
DECL|field|ENVIRONMENT_VARIABLES
specifier|public
specifier|static
specifier|final
name|String
name|ENVIRONMENT_VARIABLES
init|=
literal|"http://camel.apache.org/xml/variables/environment-variables"
decl_stmt|;
DECL|field|EXCHANGE_PROPERTY
specifier|public
specifier|static
specifier|final
name|String
name|EXCHANGE_PROPERTY
init|=
literal|"http://camel.apache.org/xml/variables/exchange-property"
decl_stmt|;
DECL|field|namespaces
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * Creates an empty namespaces object      */
DECL|method|Namespaces ()
specifier|public
name|Namespaces
parameter_list|()
block|{     }
comment|/**      * Creates a namespace context with a single prefix and URI      */
DECL|method|Namespaces (String prefix, String uri)
specifier|public
name|Namespaces
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|add
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns true if the given namespaceURI is empty or if it matches the      * given expected namespace      */
DECL|method|isMatchingNamespaceOrEmptyNamespace (String namespaceURI, String expectedNamespace)
specifier|public
specifier|static
name|boolean
name|isMatchingNamespaceOrEmptyNamespace
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|expectedNamespace
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|namespaceURI
argument_list|)
operator|||
name|namespaceURI
operator|.
name|equals
argument_list|(
name|expectedNamespace
argument_list|)
return|;
block|}
DECL|method|add (String prefix, String uri)
specifier|public
name|Namespaces
name|add
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|namespaces
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getNamespaces ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNamespaces
parameter_list|()
block|{
return|return
name|namespaces
return|;
block|}
comment|/**      * Configures the namespace aware object      */
DECL|method|configure (NamespaceAware namespaceAware)
specifier|public
name|void
name|configure
parameter_list|(
name|NamespaceAware
name|namespaceAware
parameter_list|)
block|{
name|namespaceAware
operator|.
name|setNamespaces
argument_list|(
name|getNamespaces
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

