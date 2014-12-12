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
block|}
end_class

end_unit

