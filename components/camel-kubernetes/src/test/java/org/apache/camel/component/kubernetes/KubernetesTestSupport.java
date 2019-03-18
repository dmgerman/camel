begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_class
DECL|class|KubernetesTestSupport
specifier|public
class|class
name|KubernetesTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|authToken
specifier|protected
name|String
name|authToken
decl_stmt|;
DECL|field|host
specifier|protected
name|String
name|host
decl_stmt|;
comment|// The Camel-Kubernetes tests are based on vagrant fabric8-image
comment|// https://github.com/fabric8io/fabric8-installer/tree/master/vagrant/openshift
comment|// by running the vagrant image you'll have an environment with
comment|// Openshift/Kubernetes installed
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// INSERT credentials and host here
name|authToken
operator|=
literal|""
expr_stmt|;
name|host
operator|=
literal|"https://192.168.99.100:8443"
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|toUrlEncoded (String str)
specifier|public
specifier|static
name|String
name|toUrlEncoded
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|str
operator|.
name|replaceAll
argument_list|(
literal|"="
argument_list|,
literal|"%3D"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

