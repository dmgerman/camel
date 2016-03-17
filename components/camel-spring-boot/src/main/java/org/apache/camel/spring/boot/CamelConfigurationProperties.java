begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
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

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.springboot"
argument_list|)
DECL|class|CamelConfigurationProperties
specifier|public
class|class
name|CamelConfigurationProperties
block|{
comment|// Properties
comment|/**      * Enable JMX support for the CamelContext.      */
DECL|field|jmxEnabled
specifier|private
name|boolean
name|jmxEnabled
init|=
literal|true
decl_stmt|;
comment|/**      * Producer template endpoints cache size.      */
DECL|field|producerTemplateCacheSize
specifier|private
name|int
name|producerTemplateCacheSize
init|=
literal|1000
decl_stmt|;
comment|/**      * Consumer template endpoints cache size.      */
DECL|field|consumerTemplateCacheSize
specifier|private
name|int
name|consumerTemplateCacheSize
init|=
literal|1000
decl_stmt|;
comment|/**      * Enables enhanced Camel/Spring type conversion.      */
DECL|field|typeConversion
specifier|private
name|boolean
name|typeConversion
init|=
literal|true
decl_stmt|;
comment|/**      * Sets the name of the this CamelContext.      */
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
comment|/**      * Directory to scan for adding additional XML routes.      * You can turn this off by setting the value to<tt>false</tt>      */
DECL|field|xmlRoutes
specifier|private
name|String
name|xmlRoutes
init|=
literal|"classpath:camel/*.xml"
decl_stmt|;
comment|/**      * Directory to scan for adding additional XML rests.      * You can turn this off by setting the value to<tt>false</tt>      */
DECL|field|xmlRests
specifier|private
name|String
name|xmlRests
init|=
literal|"classpath:camel-rest/*.xml"
decl_stmt|;
comment|/**      * Whether to use the main run controller to ensure the Spring-Boot application      * keeps running until being stopped or the JVM terminated.      */
DECL|field|mainRunController
specifier|private
name|boolean
name|mainRunController
decl_stmt|;
comment|// Getters& setters
DECL|method|isJmxEnabled ()
specifier|public
name|boolean
name|isJmxEnabled
parameter_list|()
block|{
return|return
name|jmxEnabled
return|;
block|}
DECL|method|setJmxEnabled (boolean jmxEnabled)
specifier|public
name|void
name|setJmxEnabled
parameter_list|(
name|boolean
name|jmxEnabled
parameter_list|)
block|{
name|this
operator|.
name|jmxEnabled
operator|=
name|jmxEnabled
expr_stmt|;
block|}
DECL|method|getProducerTemplateCacheSize ()
specifier|public
name|int
name|getProducerTemplateCacheSize
parameter_list|()
block|{
return|return
name|producerTemplateCacheSize
return|;
block|}
DECL|method|setProducerTemplateCacheSize (int producerTemplateCacheSize)
specifier|public
name|void
name|setProducerTemplateCacheSize
parameter_list|(
name|int
name|producerTemplateCacheSize
parameter_list|)
block|{
name|this
operator|.
name|producerTemplateCacheSize
operator|=
name|producerTemplateCacheSize
expr_stmt|;
block|}
DECL|method|getConsumerTemplateCacheSize ()
specifier|public
name|int
name|getConsumerTemplateCacheSize
parameter_list|()
block|{
return|return
name|consumerTemplateCacheSize
return|;
block|}
DECL|method|setConsumerTemplateCacheSize (int consumerTemplateCacheSize)
specifier|public
name|void
name|setConsumerTemplateCacheSize
parameter_list|(
name|int
name|consumerTemplateCacheSize
parameter_list|)
block|{
name|this
operator|.
name|consumerTemplateCacheSize
operator|=
name|consumerTemplateCacheSize
expr_stmt|;
block|}
DECL|method|isTypeConversion ()
specifier|public
name|boolean
name|isTypeConversion
parameter_list|()
block|{
return|return
name|typeConversion
return|;
block|}
DECL|method|setTypeConversion (boolean typeConversion)
specifier|public
name|void
name|setTypeConversion
parameter_list|(
name|boolean
name|typeConversion
parameter_list|)
block|{
name|this
operator|.
name|typeConversion
operator|=
name|typeConversion
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getXmlRoutes ()
specifier|public
name|String
name|getXmlRoutes
parameter_list|()
block|{
return|return
name|xmlRoutes
return|;
block|}
DECL|method|setXmlRoutes (String xmlRoutes)
specifier|public
name|void
name|setXmlRoutes
parameter_list|(
name|String
name|xmlRoutes
parameter_list|)
block|{
name|this
operator|.
name|xmlRoutes
operator|=
name|xmlRoutes
expr_stmt|;
block|}
DECL|method|getXmlRests ()
specifier|public
name|String
name|getXmlRests
parameter_list|()
block|{
return|return
name|xmlRests
return|;
block|}
DECL|method|setXmlRests (String xmlRests)
specifier|public
name|void
name|setXmlRests
parameter_list|(
name|String
name|xmlRests
parameter_list|)
block|{
name|this
operator|.
name|xmlRests
operator|=
name|xmlRests
expr_stmt|;
block|}
DECL|method|isMainRunController ()
specifier|public
name|boolean
name|isMainRunController
parameter_list|()
block|{
return|return
name|mainRunController
return|;
block|}
DECL|method|setMainRunController (boolean mainRunController)
specifier|public
name|void
name|setMainRunController
parameter_list|(
name|boolean
name|mainRunController
parameter_list|)
block|{
name|this
operator|.
name|mainRunController
operator|=
name|mainRunController
expr_stmt|;
block|}
block|}
end_class

end_unit

