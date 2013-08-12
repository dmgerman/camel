begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|model
operator|.
name|IdentifiedType
import|;
end_import

begin_comment
comment|/**  * The JAXB type class for the configuration of stream caching  *  * @version   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"streamCaching"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CamelStreamCachingStrategyDefinition
specifier|public
class|class
name|CamelStreamCachingStrategyDefinition
extends|extends
name|IdentifiedType
block|{
annotation|@
name|XmlAttribute
DECL|field|enabled
specifier|private
name|String
name|enabled
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|spoolDirectory
specifier|private
name|String
name|spoolDirectory
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|spoolChiper
specifier|private
name|String
name|spoolChiper
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|spoolThreshold
specifier|private
name|String
name|spoolThreshold
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|spoolUsedHeapMemoryThreshold
specifier|private
name|String
name|spoolUsedHeapMemoryThreshold
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|spoolUsedHeapMemoryLimit
specifier|private
name|String
name|spoolUsedHeapMemoryLimit
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|spoolRules
specifier|private
name|String
name|spoolRules
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|bufferSize
specifier|private
name|String
name|bufferSize
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|removeSpoolDirectoryWhenStopping
specifier|private
name|String
name|removeSpoolDirectoryWhenStopping
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|statisticsEnabled
specifier|private
name|String
name|statisticsEnabled
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|anySpoolRules
specifier|private
name|String
name|anySpoolRules
decl_stmt|;
DECL|method|getEnabled ()
specifier|public
name|String
name|getEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (String enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|String
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|getSpoolDirectory ()
specifier|public
name|String
name|getSpoolDirectory
parameter_list|()
block|{
return|return
name|spoolDirectory
return|;
block|}
DECL|method|setSpoolDirectory (String spoolDirectory)
specifier|public
name|void
name|setSpoolDirectory
parameter_list|(
name|String
name|spoolDirectory
parameter_list|)
block|{
name|this
operator|.
name|spoolDirectory
operator|=
name|spoolDirectory
expr_stmt|;
block|}
DECL|method|getSpoolChiper ()
specifier|public
name|String
name|getSpoolChiper
parameter_list|()
block|{
return|return
name|spoolChiper
return|;
block|}
DECL|method|setSpoolChiper (String spoolChiper)
specifier|public
name|void
name|setSpoolChiper
parameter_list|(
name|String
name|spoolChiper
parameter_list|)
block|{
name|this
operator|.
name|spoolChiper
operator|=
name|spoolChiper
expr_stmt|;
block|}
DECL|method|getSpoolThreshold ()
specifier|public
name|String
name|getSpoolThreshold
parameter_list|()
block|{
return|return
name|spoolThreshold
return|;
block|}
DECL|method|setSpoolThreshold (String spoolThreshold)
specifier|public
name|void
name|setSpoolThreshold
parameter_list|(
name|String
name|spoolThreshold
parameter_list|)
block|{
name|this
operator|.
name|spoolThreshold
operator|=
name|spoolThreshold
expr_stmt|;
block|}
DECL|method|getSpoolUsedHeapMemoryThreshold ()
specifier|public
name|String
name|getSpoolUsedHeapMemoryThreshold
parameter_list|()
block|{
return|return
name|spoolUsedHeapMemoryThreshold
return|;
block|}
DECL|method|setSpoolUsedHeapMemoryThreshold (String spoolUsedHeapMemoryThreshold)
specifier|public
name|void
name|setSpoolUsedHeapMemoryThreshold
parameter_list|(
name|String
name|spoolUsedHeapMemoryThreshold
parameter_list|)
block|{
name|this
operator|.
name|spoolUsedHeapMemoryThreshold
operator|=
name|spoolUsedHeapMemoryThreshold
expr_stmt|;
block|}
DECL|method|getSpoolUsedHeapMemoryLimit ()
specifier|public
name|String
name|getSpoolUsedHeapMemoryLimit
parameter_list|()
block|{
return|return
name|spoolUsedHeapMemoryLimit
return|;
block|}
DECL|method|setSpoolUsedHeapMemoryLimit (String spoolUsedHeapMemoryLimit)
specifier|public
name|void
name|setSpoolUsedHeapMemoryLimit
parameter_list|(
name|String
name|spoolUsedHeapMemoryLimit
parameter_list|)
block|{
name|this
operator|.
name|spoolUsedHeapMemoryLimit
operator|=
name|spoolUsedHeapMemoryLimit
expr_stmt|;
block|}
DECL|method|getSpoolRules ()
specifier|public
name|String
name|getSpoolRules
parameter_list|()
block|{
return|return
name|spoolRules
return|;
block|}
DECL|method|setSpoolRules (String spoolRules)
specifier|public
name|void
name|setSpoolRules
parameter_list|(
name|String
name|spoolRules
parameter_list|)
block|{
name|this
operator|.
name|spoolRules
operator|=
name|spoolRules
expr_stmt|;
block|}
DECL|method|getBufferSize ()
specifier|public
name|String
name|getBufferSize
parameter_list|()
block|{
return|return
name|bufferSize
return|;
block|}
DECL|method|setBufferSize (String bufferSize)
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|String
name|bufferSize
parameter_list|)
block|{
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
expr_stmt|;
block|}
DECL|method|getRemoveSpoolDirectoryWhenStopping ()
specifier|public
name|String
name|getRemoveSpoolDirectoryWhenStopping
parameter_list|()
block|{
return|return
name|removeSpoolDirectoryWhenStopping
return|;
block|}
DECL|method|setRemoveSpoolDirectoryWhenStopping (String removeSpoolDirectoryWhenStopping)
specifier|public
name|void
name|setRemoveSpoolDirectoryWhenStopping
parameter_list|(
name|String
name|removeSpoolDirectoryWhenStopping
parameter_list|)
block|{
name|this
operator|.
name|removeSpoolDirectoryWhenStopping
operator|=
name|removeSpoolDirectoryWhenStopping
expr_stmt|;
block|}
DECL|method|getStatisticsEnabled ()
specifier|public
name|String
name|getStatisticsEnabled
parameter_list|()
block|{
return|return
name|statisticsEnabled
return|;
block|}
DECL|method|setStatisticsEnabled (String statisticsEnabled)
specifier|public
name|void
name|setStatisticsEnabled
parameter_list|(
name|String
name|statisticsEnabled
parameter_list|)
block|{
name|this
operator|.
name|statisticsEnabled
operator|=
name|statisticsEnabled
expr_stmt|;
block|}
DECL|method|getAnySpoolRules ()
specifier|public
name|String
name|getAnySpoolRules
parameter_list|()
block|{
return|return
name|anySpoolRules
return|;
block|}
DECL|method|setAnySpoolRules (String anySpoolRules)
specifier|public
name|void
name|setAnySpoolRules
parameter_list|(
name|String
name|anySpoolRules
parameter_list|)
block|{
name|this
operator|.
name|anySpoolRules
operator|=
name|anySpoolRules
expr_stmt|;
block|}
block|}
end_class

end_unit

