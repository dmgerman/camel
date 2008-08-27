begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.ldap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ldap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|SearchControls
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
name|CamelException
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|impl
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * Represents an endpoint that synchronously invokes an LDAP server when a producer sends a message to it.  *   * @version   */
end_comment

begin_class
DECL|class|LdapEndpoint
specifier|public
class|class
name|LdapEndpoint
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|DefaultEndpoint
argument_list|<
name|E
argument_list|>
block|{
DECL|field|OBJECT_SCOPE
specifier|public
specifier|static
specifier|final
name|String
name|OBJECT_SCOPE
init|=
literal|"object"
decl_stmt|;
DECL|field|ONELEVEL_SCOPE
specifier|public
specifier|static
specifier|final
name|String
name|ONELEVEL_SCOPE
init|=
literal|"onelevel"
decl_stmt|;
DECL|field|SUBTREE_SCOPE
specifier|public
specifier|static
specifier|final
name|String
name|SUBTREE_SCOPE
init|=
literal|"subtree"
decl_stmt|;
DECL|field|remaining
specifier|private
name|String
name|remaining
decl_stmt|;
DECL|field|base
specifier|private
name|String
name|base
decl_stmt|;
DECL|field|scope
specifier|private
name|String
name|scope
init|=
name|SUBTREE_SCOPE
decl_stmt|;
DECL|method|LdapEndpoint (String endpointUri, String remaining, LdapComponent component)
specifier|protected
name|LdapEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|LdapComponent
name|component
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|remaining
operator|=
name|remaining
expr_stmt|;
block|}
DECL|method|LdapEndpoint (String endpointUri, String remaining)
specifier|public
name|LdapEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|remaining
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|remaining
operator|=
name|remaining
expr_stmt|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
argument_list|<
name|E
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"An LDAP Consumer would be the LDAP server itself! No such support here"
argument_list|)
throw|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|E
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|LdapProducer
argument_list|(
name|this
argument_list|,
name|remaining
argument_list|,
name|base
argument_list|,
name|toSearchControlScope
argument_list|(
name|scope
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getBase ()
specifier|public
name|String
name|getBase
parameter_list|()
block|{
return|return
name|base
return|;
block|}
DECL|method|setBase (String base)
specifier|public
name|void
name|setBase
parameter_list|(
name|String
name|base
parameter_list|)
block|{
name|this
operator|.
name|base
operator|=
name|base
expr_stmt|;
block|}
DECL|method|getScope ()
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
DECL|method|setScope (String scope)
specifier|public
name|void
name|setScope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
DECL|method|toSearchControlScope (String scope)
specifier|private
name|int
name|toSearchControlScope
parameter_list|(
name|String
name|scope
parameter_list|)
throws|throws
name|CamelException
block|{
if|if
condition|(
name|scope
operator|.
name|equalsIgnoreCase
argument_list|(
name|OBJECT_SCOPE
argument_list|)
condition|)
block|{
return|return
name|SearchControls
operator|.
name|OBJECT_SCOPE
return|;
block|}
elseif|else
if|if
condition|(
name|scope
operator|.
name|equalsIgnoreCase
argument_list|(
name|ONELEVEL_SCOPE
argument_list|)
condition|)
block|{
return|return
name|SearchControls
operator|.
name|ONELEVEL_SCOPE
return|;
block|}
elseif|else
if|if
condition|(
name|scope
operator|.
name|equalsIgnoreCase
argument_list|(
name|SUBTREE_SCOPE
argument_list|)
condition|)
block|{
return|return
name|SearchControls
operator|.
name|SUBTREE_SCOPE
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Invalid search scope \""
operator|+
name|scope
operator|+
literal|"\" for LdapEndpoint: "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

