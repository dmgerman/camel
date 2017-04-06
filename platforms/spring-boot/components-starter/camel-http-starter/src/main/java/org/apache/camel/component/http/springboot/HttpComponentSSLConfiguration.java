begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * SSL related options  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.http.ssl"
argument_list|)
DECL|class|HttpComponentSSLConfiguration
specifier|public
class|class
name|HttpComponentSSLConfiguration
block|{
comment|/**      * Auto-configure SSL from SSLContextParameters.      */
DECL|field|autoConfigure
specifier|private
name|boolean
name|autoConfigure
init|=
literal|true
decl_stmt|;
DECL|method|HttpComponentSSLConfiguration ()
specifier|public
name|HttpComponentSSLConfiguration
parameter_list|()
block|{     }
DECL|method|isAutoConfigure ()
specifier|public
name|boolean
name|isAutoConfigure
parameter_list|()
block|{
return|return
name|autoConfigure
return|;
block|}
DECL|method|setAutoConfigure (boolean autoConfigure)
specifier|public
name|void
name|setAutoConfigure
parameter_list|(
name|boolean
name|autoConfigure
parameter_list|)
block|{
name|this
operator|.
name|autoConfigure
operator|=
name|autoConfigure
expr_stmt|;
block|}
block|}
end_class

end_unit

