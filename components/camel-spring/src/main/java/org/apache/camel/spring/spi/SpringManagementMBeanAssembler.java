begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|spi
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
name|CamelContext
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
name|management
operator|.
name|DefaultManagementMBeanAssembler
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
name|management
operator|.
name|DefaultRequiredModelMBean
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
name|management
operator|.
name|NotificationSenderAdapter
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|AnnotationJmxAttributeSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|assembler
operator|.
name|MetadataMBeanInfoAssembler
import|;
end_import

begin_comment
comment|/**  * An springAssembler to assemble a {@link javax.management.modelmbean.ModelMBean} which can be used  * to register the object in JMX. The springAssembler is capable of using the Spring JMX annotations to  * gather the list of JMX operations and attributes.  */
end_comment

begin_class
DECL|class|SpringManagementMBeanAssembler
specifier|public
class|class
name|SpringManagementMBeanAssembler
extends|extends
name|DefaultManagementMBeanAssembler
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SpringManagementMBeanAssembler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|springAssembler
specifier|private
specifier|final
name|MetadataMBeanInfoAssembler
name|springAssembler
decl_stmt|;
DECL|method|SpringManagementMBeanAssembler (CamelContext camelContext)
specifier|public
name|SpringManagementMBeanAssembler
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|springAssembler
operator|=
operator|new
name|MetadataMBeanInfoAssembler
argument_list|()
expr_stmt|;
name|this
operator|.
name|springAssembler
operator|.
name|setAttributeSource
argument_list|(
operator|new
name|AnnotationJmxAttributeSource
argument_list|()
argument_list|)
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
comment|// prefer to use the managed instance if it has been annotated with Spring JMX annotations
if|if
condition|(
name|obj
operator|instanceof
name|ManagedInstance
condition|)
block|{
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
name|LOG
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
name|springAssembler
operator|.
name|getMBeanInfo
argument_list|(
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
if|if
condition|(
name|ObjectHelper
operator|.
name|hasAnnotation
argument_list|(
name|obj
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
comment|// the object has a Spring ManagedResource annotations so assemble the MBeanInfo
name|LOG
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
name|springAssembler
operator|.
name|getMBeanInfo
argument_list|(
name|obj
argument_list|,
name|name
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// fallback and let the default mbean assembler handle this instead
return|return
name|super
operator|.
name|assemble
argument_list|(
name|mBeanServer
argument_list|,
name|obj
argument_list|,
name|name
argument_list|)
return|;
block|}
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Assembled MBeanInfo {}"
argument_list|,
name|mbi
argument_list|)
expr_stmt|;
name|boolean
name|santizie
init|=
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getSanitize
argument_list|()
operator|!=
literal|null
operator|&&
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getSanitize
argument_list|()
decl_stmt|;
name|RequiredModelMBean
name|mbean
init|=
operator|new
name|DefaultRequiredModelMBean
argument_list|(
name|mbi
argument_list|,
name|santizie
argument_list|)
decl_stmt|;
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
block|}
end_class

end_unit

