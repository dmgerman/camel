begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.eks
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|eks
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|eks
operator|.
name|AmazonEKS
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
name|spi
operator|.
name|Metadata
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
name|UriParams
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
name|UriPath
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|EKSConfiguration
specifier|public
class|class
name|EKSConfiguration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Logical name"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|label
specifier|private
name|String
name|label
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|eksClient
specifier|private
name|AmazonEKS
name|eksClient
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|operation
specifier|private
name|EKSOperations
name|operation
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
annotation|@
name|UriParam
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
DECL|method|getEksClient ()
specifier|public
name|AmazonEKS
name|getEksClient
parameter_list|()
block|{
return|return
name|eksClient
return|;
block|}
comment|/**      * To use a existing configured AWS EKS as client      */
DECL|method|setEksClient (AmazonEKS eksClient)
specifier|public
name|void
name|setEksClient
parameter_list|(
name|AmazonEKS
name|eksClient
parameter_list|)
block|{
name|this
operator|.
name|eksClient
operator|=
name|eksClient
expr_stmt|;
block|}
DECL|method|getAccessKey ()
specifier|public
name|String
name|getAccessKey
parameter_list|()
block|{
return|return
name|accessKey
return|;
block|}
comment|/**      * Amazon AWS Access Key      */
DECL|method|setAccessKey (String accessKey)
specifier|public
name|void
name|setAccessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|this
operator|.
name|accessKey
operator|=
name|accessKey
expr_stmt|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|String
name|getSecretKey
parameter_list|()
block|{
return|return
name|secretKey
return|;
block|}
comment|/**      * Amazon AWS Secret Key      */
DECL|method|setSecretKey (String secretKey)
specifier|public
name|void
name|setSecretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|this
operator|.
name|secretKey
operator|=
name|secretKey
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|EKSOperations
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * The operation to perform      */
DECL|method|setOperation (EKSOperations operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|EKSOperations
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getProxyHost ()
specifier|public
name|String
name|getProxyHost
parameter_list|()
block|{
return|return
name|proxyHost
return|;
block|}
comment|/**      * To define a proxy host when instantiating the EKS client      */
DECL|method|setProxyHost (String proxyHost)
specifier|public
name|void
name|setProxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|this
operator|.
name|proxyHost
operator|=
name|proxyHost
expr_stmt|;
block|}
DECL|method|getProxyPort ()
specifier|public
name|Integer
name|getProxyPort
parameter_list|()
block|{
return|return
name|proxyPort
return|;
block|}
comment|/**      * To define a proxy port when instantiating the EKS client      */
DECL|method|setProxyPort (Integer proxyPort)
specifier|public
name|void
name|setProxyPort
parameter_list|(
name|Integer
name|proxyPort
parameter_list|)
block|{
name|this
operator|.
name|proxyPort
operator|=
name|proxyPort
expr_stmt|;
block|}
DECL|method|getRegion ()
specifier|public
name|String
name|getRegion
parameter_list|()
block|{
return|return
name|region
return|;
block|}
comment|/**      * The region in which EKS client needs to work      */
DECL|method|setRegion (String region)
specifier|public
name|void
name|setRegion
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|this
operator|.
name|region
operator|=
name|region
expr_stmt|;
block|}
comment|// *************************************************
comment|//
comment|// *************************************************
DECL|method|copy ()
specifier|public
name|EKSConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|EKSConfiguration
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

