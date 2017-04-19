begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openshift.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openshift
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

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
comment|/**  * To manage your Openshift 2.x applications.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.openshift"
argument_list|)
DECL|class|OpenShiftComponentConfiguration
specifier|public
class|class
name|OpenShiftComponentConfiguration
block|{
comment|/**      * The username to login to openshift server.      */
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
comment|/**      * The password for login to openshift server.      */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**      * Domain name. If not specified then the default domain is used.      */
DECL|field|domain
specifier|private
name|String
name|domain
decl_stmt|;
comment|/**      * Url to the openshift server. If not specified then the default value from      * the local openshift configuration file /.openshift/express.conf is used.      * And if that fails as well then openshift.redhat.com is used.      */
DECL|field|server
specifier|private
name|String
name|server
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getDomain ()
specifier|public
name|String
name|getDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
DECL|method|setDomain (String domain)
specifier|public
name|void
name|setDomain
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
block|}
DECL|method|getServer ()
specifier|public
name|String
name|getServer
parameter_list|()
block|{
return|return
name|server
return|;
block|}
DECL|method|setServer (String server)
specifier|public
name|void
name|setServer
parameter_list|(
name|String
name|server
parameter_list|)
block|{
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

