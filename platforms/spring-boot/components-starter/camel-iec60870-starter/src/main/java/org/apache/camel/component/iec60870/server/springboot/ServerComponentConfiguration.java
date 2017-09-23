begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iec60870.server.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iec60870
operator|.
name|server
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
comment|/**  * IEC 60870 component used for telecontrol (supervisory control and data  * acquisition) such as controlling electric power transmission grids and other  * geographically widespread control systems.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.iec60870-server"
argument_list|)
DECL|class|ServerComponentConfiguration
specifier|public
class|class
name|ServerComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Default connection options      */
DECL|field|defaultConnectionOptions
specifier|private
name|ServerOptionsNestedConfiguration
name|defaultConnectionOptions
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getDefaultConnectionOptions ()
specifier|public
name|ServerOptionsNestedConfiguration
name|getDefaultConnectionOptions
parameter_list|()
block|{
return|return
name|defaultConnectionOptions
return|;
block|}
DECL|method|setDefaultConnectionOptions ( ServerOptionsNestedConfiguration defaultConnectionOptions)
specifier|public
name|void
name|setDefaultConnectionOptions
parameter_list|(
name|ServerOptionsNestedConfiguration
name|defaultConnectionOptions
parameter_list|)
block|{
name|this
operator|.
name|defaultConnectionOptions
operator|=
name|defaultConnectionOptions
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
DECL|class|ServerOptionsNestedConfiguration
specifier|public
specifier|static
class|class
name|ServerOptionsNestedConfiguration
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
name|iec60870
operator|.
name|server
operator|.
name|ServerOptions
operator|.
name|class
decl_stmt|;
comment|/**          * A time period in "ms" the protocol layer will buffer change events in          * order to send out aggregated change messages          */
DECL|field|bufferingPeriod
specifier|private
name|Integer
name|bufferingPeriod
decl_stmt|;
comment|/**          * Send booleans with timestamps          */
DECL|field|booleansWithTimestamp
specifier|private
name|Boolean
name|booleansWithTimestamp
decl_stmt|;
comment|/**          * Send floats with timestamps          */
DECL|field|floatsWithTimestamp
specifier|private
name|Boolean
name|floatsWithTimestamp
decl_stmt|;
comment|/**          * Number of spontaneous events to keep in the buffer.          *<p>          * When there are more than this number of spontaneous in events in the          * buffer, then events will be dropped in order to maintain the buffer          * size.          *</p>          */
DECL|field|spontaneousDuplicates
specifier|private
name|Integer
name|spontaneousDuplicates
decl_stmt|;
comment|/**          * The period in "ms" between background transmission cycles.          *<p>          * If this is set to zero or less, background transmissions will be          * disabled.          *</p>          */
DECL|field|backgroundScanPeriod
specifier|private
name|Integer
name|backgroundScanPeriod
decl_stmt|;
DECL|method|getBufferingPeriod ()
specifier|public
name|Integer
name|getBufferingPeriod
parameter_list|()
block|{
return|return
name|bufferingPeriod
return|;
block|}
DECL|method|setBufferingPeriod (Integer bufferingPeriod)
specifier|public
name|void
name|setBufferingPeriod
parameter_list|(
name|Integer
name|bufferingPeriod
parameter_list|)
block|{
name|this
operator|.
name|bufferingPeriod
operator|=
name|bufferingPeriod
expr_stmt|;
block|}
DECL|method|getBooleansWithTimestamp ()
specifier|public
name|Boolean
name|getBooleansWithTimestamp
parameter_list|()
block|{
return|return
name|booleansWithTimestamp
return|;
block|}
DECL|method|setBooleansWithTimestamp (Boolean booleansWithTimestamp)
specifier|public
name|void
name|setBooleansWithTimestamp
parameter_list|(
name|Boolean
name|booleansWithTimestamp
parameter_list|)
block|{
name|this
operator|.
name|booleansWithTimestamp
operator|=
name|booleansWithTimestamp
expr_stmt|;
block|}
DECL|method|getFloatsWithTimestamp ()
specifier|public
name|Boolean
name|getFloatsWithTimestamp
parameter_list|()
block|{
return|return
name|floatsWithTimestamp
return|;
block|}
DECL|method|setFloatsWithTimestamp (Boolean floatsWithTimestamp)
specifier|public
name|void
name|setFloatsWithTimestamp
parameter_list|(
name|Boolean
name|floatsWithTimestamp
parameter_list|)
block|{
name|this
operator|.
name|floatsWithTimestamp
operator|=
name|floatsWithTimestamp
expr_stmt|;
block|}
DECL|method|getSpontaneousDuplicates ()
specifier|public
name|Integer
name|getSpontaneousDuplicates
parameter_list|()
block|{
return|return
name|spontaneousDuplicates
return|;
block|}
DECL|method|setSpontaneousDuplicates (Integer spontaneousDuplicates)
specifier|public
name|void
name|setSpontaneousDuplicates
parameter_list|(
name|Integer
name|spontaneousDuplicates
parameter_list|)
block|{
name|this
operator|.
name|spontaneousDuplicates
operator|=
name|spontaneousDuplicates
expr_stmt|;
block|}
DECL|method|getBackgroundScanPeriod ()
specifier|public
name|Integer
name|getBackgroundScanPeriod
parameter_list|()
block|{
return|return
name|backgroundScanPeriod
return|;
block|}
DECL|method|setBackgroundScanPeriod (Integer backgroundScanPeriod)
specifier|public
name|void
name|setBackgroundScanPeriod
parameter_list|(
name|Integer
name|backgroundScanPeriod
parameter_list|)
block|{
name|this
operator|.
name|backgroundScanPeriod
operator|=
name|backgroundScanPeriod
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

