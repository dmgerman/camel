begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.jsse
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|jsse
package|;
end_package

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

begin_comment
comment|/**  * Represents a list of TLS/SSL cipher suite names.  */
end_comment

begin_class
DECL|class|SecureSocketProtocolsParameters
specifier|public
class|class
name|SecureSocketProtocolsParameters
block|{
DECL|field|secureSocketProtocol
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|secureSocketProtocol
decl_stmt|;
comment|/**      * Returns a live reference to the list of secure socket protocol names.      *      * @return a reference to the list, never {@code null}      */
DECL|method|getSecureSocketProtocol ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSecureSocketProtocol
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|secureSocketProtocol
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|secureSocketProtocol
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|secureSocketProtocol
return|;
block|}
comment|/**      * Sets the list of secure socket protocol names. It creates a copy of the given protocol list.      *      * @param secureSocketProtocol list of secure socket protocol names      */
DECL|method|setSecureSocketProtocol (List<String> secureSocketProtocol)
specifier|public
name|void
name|setSecureSocketProtocol
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|secureSocketProtocol
parameter_list|)
block|{
name|this
operator|.
name|secureSocketProtocol
operator|=
name|secureSocketProtocol
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|secureSocketProtocol
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"SecureSocketProtocolsParameters[secureSocketProtocol="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|getSecureSocketProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

