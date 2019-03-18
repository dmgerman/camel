begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|SmartFactoryBean
import|;
end_import

begin_comment
comment|/**  * This factoryBean which can help user to choice CXF components that he wants bus to load  * without needing to import bunch of CXF packages in OSGi bundle, as the SpringBusFactory  * will try to load the bus extensions with the CXF bundle classloader.  * You can set the CXF extensions files with ; as the separator to create a bus.  *   * NOTE: when you set the includeDefaultBus value to be false, you should aware that the CXF bus  * will automatically load all the extension in CXF 2.4.x by default.    * You can still specify the spring extension file in the cfgFiles list and it will override   * the extensions which is load by CXF bus.  */
end_comment

begin_class
DECL|class|SpringBusFactoryBean
specifier|public
class|class
name|SpringBusFactoryBean
implements|implements
name|SmartFactoryBean
argument_list|<
name|Bus
argument_list|>
block|{
DECL|field|cfgFiles
specifier|private
name|String
index|[]
name|cfgFiles
decl_stmt|;
DECL|field|includeDefaultBus
specifier|private
name|boolean
name|includeDefaultBus
decl_stmt|;
DECL|field|bf
specifier|private
name|SpringBusFactory
name|bf
decl_stmt|;
DECL|method|getObject ()
specifier|public
name|Bus
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
name|bf
operator|=
operator|new
name|SpringBusFactory
argument_list|()
expr_stmt|;
if|if
condition|(
name|cfgFiles
operator|!=
literal|null
condition|)
block|{
return|return
name|bf
operator|.
name|createBus
argument_list|(
name|cfgFiles
argument_list|,
name|includeDefaultBus
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|bf
operator|.
name|createBus
argument_list|()
return|;
block|}
block|}
DECL|method|getObjectType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getObjectType
parameter_list|()
block|{
return|return
name|Bus
operator|.
name|class
return|;
block|}
DECL|method|setCfgFiles (String cfgFiles)
specifier|public
name|void
name|setCfgFiles
parameter_list|(
name|String
name|cfgFiles
parameter_list|)
block|{
name|this
operator|.
name|cfgFiles
operator|=
name|cfgFiles
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
expr_stmt|;
block|}
DECL|method|setIncludeDefaultBus (boolean includeDefaultBus)
specifier|public
name|void
name|setIncludeDefaultBus
parameter_list|(
name|boolean
name|includeDefaultBus
parameter_list|)
block|{
name|this
operator|.
name|includeDefaultBus
operator|=
name|includeDefaultBus
expr_stmt|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isEagerInit ()
specifier|public
name|boolean
name|isEagerInit
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isPrototype ()
specifier|public
name|boolean
name|isPrototype
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

