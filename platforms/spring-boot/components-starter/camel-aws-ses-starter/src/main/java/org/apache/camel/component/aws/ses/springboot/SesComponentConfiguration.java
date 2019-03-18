begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ses.springboot
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
name|ses
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|AmazonSimpleEmailService
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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
comment|/**  * The aws-ses component is used for sending emails with Amazon's SES service.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.aws-ses"
argument_list|)
DECL|class|SesComponentConfiguration
specifier|public
class|class
name|SesComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the aws-ses component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * The AWS SES default configuration      */
DECL|field|configuration
specifier|private
name|SesConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Amazon AWS Access Key      */
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
comment|/**      * Amazon AWS Secret Key      */
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
comment|/**      * The region in which SES client needs to work      */
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|SesConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( SesConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|SesConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
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
DECL|class|SesConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|SesConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
name|ses
operator|.
name|SesConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * Amazon AWS Access Key          */
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
comment|/**          * To use the AmazonSimpleEmailService as the client          */
DECL|field|amazonSESClient
specifier|private
name|AmazonSimpleEmailService
name|amazonSESClient
decl_stmt|;
comment|/**          * The sender's email address.          */
DECL|field|from
specifier|private
name|String
name|from
decl_stmt|;
comment|/**          * List of destination email address. Can be overriden with          * 'CamelAwsSesTo' header.          */
DECL|field|to
specifier|private
name|List
name|to
decl_stmt|;
comment|/**          * Amazon AWS Secret Key          */
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
comment|/**          * The subject which is used if the message header 'CamelAwsSesSubject'          * is not present.          */
DECL|field|subject
specifier|private
name|String
name|subject
decl_stmt|;
comment|/**          * The email address to which bounce notifications are to be forwarded,          * override it using 'CamelAwsSesReturnPath' header.          */
DECL|field|returnPath
specifier|private
name|String
name|returnPath
decl_stmt|;
comment|/**          * List of reply-to email address(es) for the message, override it using          * 'CamelAwsSesReplyToAddresses' header.          */
DECL|field|replyToAddresses
specifier|private
name|List
name|replyToAddresses
decl_stmt|;
comment|/**          * To define a proxy host when instantiating the SES client          */
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
comment|/**          * To define a proxy port when instantiating the SES client          */
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
comment|/**          * The region in which SES client needs to work          */
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
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
DECL|method|getAmazonSESClient ()
specifier|public
name|AmazonSimpleEmailService
name|getAmazonSESClient
parameter_list|()
block|{
return|return
name|amazonSESClient
return|;
block|}
DECL|method|setAmazonSESClient (AmazonSimpleEmailService amazonSESClient)
specifier|public
name|void
name|setAmazonSESClient
parameter_list|(
name|AmazonSimpleEmailService
name|amazonSESClient
parameter_list|)
block|{
name|this
operator|.
name|amazonSESClient
operator|=
name|amazonSESClient
expr_stmt|;
block|}
DECL|method|getFrom ()
specifier|public
name|String
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
DECL|method|setFrom (String from)
specifier|public
name|void
name|setFrom
parameter_list|(
name|String
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
block|}
DECL|method|getTo ()
specifier|public
name|List
name|getTo
parameter_list|()
block|{
return|return
name|to
return|;
block|}
DECL|method|setTo (List to)
specifier|public
name|void
name|setTo
parameter_list|(
name|List
name|to
parameter_list|)
block|{
name|this
operator|.
name|to
operator|=
name|to
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
DECL|method|getSubject ()
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
DECL|method|setSubject (String subject)
specifier|public
name|void
name|setSubject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
DECL|method|getReturnPath ()
specifier|public
name|String
name|getReturnPath
parameter_list|()
block|{
return|return
name|returnPath
return|;
block|}
DECL|method|setReturnPath (String returnPath)
specifier|public
name|void
name|setReturnPath
parameter_list|(
name|String
name|returnPath
parameter_list|)
block|{
name|this
operator|.
name|returnPath
operator|=
name|returnPath
expr_stmt|;
block|}
DECL|method|getReplyToAddresses ()
specifier|public
name|List
name|getReplyToAddresses
parameter_list|()
block|{
return|return
name|replyToAddresses
return|;
block|}
DECL|method|setReplyToAddresses (List replyToAddresses)
specifier|public
name|void
name|setReplyToAddresses
parameter_list|(
name|List
name|replyToAddresses
parameter_list|)
block|{
name|this
operator|.
name|replyToAddresses
operator|=
name|replyToAddresses
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
block|}
block|}
end_class

end_unit

