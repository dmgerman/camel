begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
package|;
end_package

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|AtomixClient
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
name|atomix
operator|.
name|AtomixConfiguration
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
name|UriParam
import|;
end_import

begin_class
DECL|class|AtomixClientConfiguration
specifier|public
class|class
name|AtomixClientConfiguration
extends|extends
name|AtomixConfiguration
argument_list|<
name|AtomixClient
argument_list|>
implements|implements
name|Cloneable
block|{
annotation|@
name|UriParam
DECL|field|resultHeader
specifier|private
name|String
name|resultHeader
decl_stmt|;
comment|// ****************************************
comment|// Properties
comment|// ****************************************
DECL|method|getResultHeader ()
specifier|public
name|String
name|getResultHeader
parameter_list|()
block|{
return|return
name|resultHeader
return|;
block|}
comment|/**      * The header that wil carry the result.      */
DECL|method|setResultHeader (String resultHeader)
specifier|public
name|void
name|setResultHeader
parameter_list|(
name|String
name|resultHeader
parameter_list|)
block|{
name|this
operator|.
name|resultHeader
operator|=
name|resultHeader
expr_stmt|;
block|}
comment|// ****************************************
comment|// Copy
comment|// ****************************************
DECL|method|copy ()
specifier|public
name|AtomixClientConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|AtomixClientConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

