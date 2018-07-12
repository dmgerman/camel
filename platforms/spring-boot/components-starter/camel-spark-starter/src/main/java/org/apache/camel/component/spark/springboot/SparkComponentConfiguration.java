begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spark.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
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
comment|/**  * The spark component can be used to send RDD or DataFrame jobs to Apache Spark  * cluster.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.spark"
argument_list|)
DECL|class|SparkComponentConfiguration
specifier|public
class|class
name|SparkComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * RDD to compute against. The option is a      * org.apache.spark.api.java.JavaRDDLike type.      */
DECL|field|rdd
specifier|private
name|String
name|rdd
decl_stmt|;
comment|/**      * Function performing action against an RDD. The option is a      * org.apache.camel.component.spark.RddCallback type.      */
DECL|field|rddCallback
specifier|private
name|String
name|rddCallback
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getRdd ()
specifier|public
name|String
name|getRdd
parameter_list|()
block|{
return|return
name|rdd
return|;
block|}
DECL|method|setRdd (String rdd)
specifier|public
name|void
name|setRdd
parameter_list|(
name|String
name|rdd
parameter_list|)
block|{
name|this
operator|.
name|rdd
operator|=
name|rdd
expr_stmt|;
block|}
DECL|method|getRddCallback ()
specifier|public
name|String
name|getRddCallback
parameter_list|()
block|{
return|return
name|rddCallback
return|;
block|}
DECL|method|setRddCallback (String rddCallback)
specifier|public
name|void
name|setRddCallback
parameter_list|(
name|String
name|rddCallback
parameter_list|)
block|{
name|this
operator|.
name|rddCallback
operator|=
name|rddCallback
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

