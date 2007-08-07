begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
package|;
end_package

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
comment|/**  * @version $Revision: $  */
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
literal|"http://activemq.apache.org/camel/schema/spring"
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
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|Namespaces ()
specifier|private
name|Namespaces
parameter_list|()
block|{             }
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
name|isNullOrBlank
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
block|}
end_class

end_unit

