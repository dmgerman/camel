begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|InvalidTargetObjectTypeException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|ModelMBean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|ModelMBeanInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|RequiredModelMBean
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
name|Service
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
name|api
operator|.
name|management
operator|.
name|ManagedInstance
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|api
operator|.
name|management
operator|.
name|NotificationSenderAware
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
name|ManagementMBeanAssembler
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
name|util
operator|.
name|ObjectHelper
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
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * An assembler to assemble a {@link javax.management.modelmbean.ModelMBean} which can be used  * to register the object in JMX. The assembler is capable of using the Camel JMX annotations to  * gather the list of JMX operations and attributes.  *  * @version   */
end_comment

begin_class
DECL|class|DefaultManagementMBeanAssembler
specifier|public
class|class
name|DefaultManagementMBeanAssembler
implements|implements
name|ManagementMBeanAssembler
implements|,
name|Service
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|assembler
specifier|private
specifier|final
name|MBeanInfoAssembler
name|assembler
decl_stmt|;
DECL|method|DefaultManagementMBeanAssembler ()
specifier|public
name|DefaultManagementMBeanAssembler
parameter_list|()
block|{
name|this
operator|.
name|assembler
operator|=
operator|new
name|MBeanInfoAssembler
argument_list|()
expr_stmt|;
block|}
DECL|method|assemble (MBeanServer mBeanServer, Object obj, ObjectName name)
specifier|public
name|ModelMBean
name|assemble
parameter_list|(
name|MBeanServer
name|mBeanServer
parameter_list|,
name|Object
name|obj
parameter_list|,
name|ObjectName
name|name
parameter_list|)
throws|throws
name|JMException
block|{
name|ModelMBeanInfo
name|mbi
init|=
literal|null
decl_stmt|;
comment|// prefer to use the managed instance if it has been annotated with JMX annotations
if|if
condition|(
name|obj
operator|instanceof
name|ManagedInstance
condition|)
block|{
comment|// there may be a custom embedded instance which have additional methods
name|Object
name|custom
init|=
operator|(
operator|(
name|ManagedInstance
operator|)
name|obj
operator|)
operator|.
name|getInstance
argument_list|()
decl_stmt|;
if|if
condition|(
name|custom
operator|!=
literal|null
operator|&&
name|ObjectHelper
operator|.
name|hasAnnotation
argument_list|(
name|custom
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotations
argument_list|()
argument_list|,
name|ManagedResource
operator|.
name|class
argument_list|)
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Assembling MBeanInfo for: {} from custom @ManagedResource object: {}"
argument_list|,
name|name
argument_list|,
name|custom
argument_list|)
expr_stmt|;
comment|// get the mbean info from the custom managed object
name|mbi
operator|=
name|assembler
operator|.
name|getMBeanInfo
argument_list|(
name|obj
argument_list|,
name|custom
argument_list|,
name|name
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// and let the custom object be registered in JMX
name|obj
operator|=
name|custom
expr_stmt|;
block|}
block|}
if|if
condition|(
name|mbi
operator|==
literal|null
condition|)
block|{
comment|// use the default provided mbean which has been annotated with JMX annotations
name|log
operator|.
name|trace
argument_list|(
literal|"Assembling MBeanInfo for: {} from @ManagedResource object: {}"
argument_list|,
name|name
argument_list|,
name|obj
argument_list|)
expr_stmt|;
name|mbi
operator|=
name|assembler
operator|.
name|getMBeanInfo
argument_list|(
name|obj
argument_list|,
literal|null
argument_list|,
name|name
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|RequiredModelMBean
name|mbean
init|=
operator|(
name|RequiredModelMBean
operator|)
name|mBeanServer
operator|.
name|instantiate
argument_list|(
name|RequiredModelMBean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|mbean
operator|.
name|setModelMBeanInfo
argument_list|(
name|mbi
argument_list|)
expr_stmt|;
try|try
block|{
name|mbean
operator|.
name|setManagedResource
argument_list|(
name|obj
argument_list|,
literal|"ObjectReference"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidTargetObjectTypeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JMException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
comment|// Allows the managed object to send notifications
if|if
condition|(
name|obj
operator|instanceof
name|NotificationSenderAware
condition|)
block|{
operator|(
operator|(
name|NotificationSenderAware
operator|)
name|obj
operator|)
operator|.
name|setNotificationSender
argument_list|(
operator|new
name|NotificationSenderAdapter
argument_list|(
name|mbean
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|mbean
return|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|assembler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|assembler
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

