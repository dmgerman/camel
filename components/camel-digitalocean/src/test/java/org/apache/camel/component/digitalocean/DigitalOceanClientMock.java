begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.digitalocean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|digitalocean
package|;
end_package

begin_import
import|import
name|com
operator|.
name|myjeeva
operator|.
name|digitalocean
operator|.
name|exception
operator|.
name|DigitalOceanException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|myjeeva
operator|.
name|digitalocean
operator|.
name|exception
operator|.
name|RequestUnsuccessfulException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|myjeeva
operator|.
name|digitalocean
operator|.
name|impl
operator|.
name|DigitalOceanClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|myjeeva
operator|.
name|digitalocean
operator|.
name|pojo
operator|.
name|Account
import|;
end_import

begin_class
DECL|class|DigitalOceanClientMock
specifier|public
class|class
name|DigitalOceanClientMock
extends|extends
name|DigitalOceanClient
block|{
DECL|method|DigitalOceanClientMock ()
specifier|public
name|DigitalOceanClientMock
parameter_list|()
block|{
name|super
argument_list|(
literal|"token"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getAccountInfo ()
specifier|public
name|Account
name|getAccountInfo
parameter_list|()
throws|throws
name|DigitalOceanException
throws|,
name|RequestUnsuccessfulException
block|{
name|Account
name|account
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|account
operator|.
name|setEmail
argument_list|(
literal|"camel@apache.org"
argument_list|)
expr_stmt|;
return|return
name|account
return|;
block|}
block|}
end_class

end_unit

