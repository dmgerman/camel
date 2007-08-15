begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|InstanceAlreadyExistsException
import|;
end_import

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
name|NotCompliantMBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectInstance
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
name|CamelContextAware
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
name|InstrumentationAgent
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
name|impl
operator|.
name|DefaultCamelContext
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
name|assembler
operator|.
name|MetadataMBeanInfoAssembler
import|;
end_import

begin_class
DECL|class|InstrumentationAgentImpl
specifier|public
class|class
name|InstrumentationAgentImpl
implements|implements
name|InstrumentationAgent
implements|,
name|CamelContextAware
block|{
DECL|field|server
specifier|private
name|MBeanServer
name|server
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|mbeans
specifier|private
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|mbeans
init|=
operator|new
name|HashSet
argument_list|<
name|ObjectName
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|assembler
name|MetadataMBeanInfoAssembler
name|assembler
decl_stmt|;
DECL|method|InstrumentationAgentImpl ()
specifier|public
name|InstrumentationAgentImpl
parameter_list|()
block|{
name|assembler
operator|=
operator|new
name|MetadataMBeanInfoAssembler
argument_list|()
expr_stmt|;
name|assembler
operator|.
name|setAttributeSource
argument_list|(
operator|new
name|AnnotationJmxAttributeSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|context
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|setMBeanServer (MBeanServer server)
specifier|public
name|void
name|setMBeanServer
parameter_list|(
name|MBeanServer
name|server
parameter_list|)
block|{
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
block|}
DECL|method|getMBeanServer ()
specifier|public
name|MBeanServer
name|getMBeanServer
parameter_list|()
block|{
return|return
name|server
return|;
block|}
DECL|method|register (Object obj, ObjectName name)
specifier|public
name|void
name|register
parameter_list|(
name|Object
name|obj
parameter_list|,
name|ObjectName
name|name
parameter_list|)
throws|throws
name|JMException
block|{
name|register
argument_list|(
name|obj
argument_list|,
name|name
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|register (Object obj, ObjectName name, boolean forceRegistration)
specifier|public
name|void
name|register
parameter_list|(
name|Object
name|obj
parameter_list|,
name|ObjectName
name|name
parameter_list|,
name|boolean
name|forceRegistration
parameter_list|)
throws|throws
name|JMException
block|{
try|try
block|{
name|registerMBeanWithServer
argument_list|(
name|obj
argument_list|,
name|name
argument_list|,
name|forceRegistration
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NotCompliantMBeanException
name|e
parameter_list|)
block|{
comment|//If this is not a "normal" MBean, then try to deploy it using JMX annotations
name|ModelMBeanInfo
name|mbi
init|=
literal|null
decl_stmt|;
name|mbi
operator|=
name|assembler
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
name|RequiredModelMBean
name|mbean
init|=
operator|(
name|RequiredModelMBean
operator|)
name|server
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
name|itotex
parameter_list|)
block|{
throw|throw
operator|new
name|JMException
argument_list|(
name|itotex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
name|registerMBeanWithServer
argument_list|(
name|mbean
argument_list|,
name|name
argument_list|,
name|forceRegistration
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|unregister (ObjectName name)
specifier|public
name|void
name|unregister
parameter_list|(
name|ObjectName
name|name
parameter_list|)
throws|throws
name|JMException
block|{ 	}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
comment|// LOG warning
return|return;
block|}
if|if
condition|(
name|context
operator|instanceof
name|DefaultCamelContext
condition|)
block|{
name|DefaultCamelContext
name|dc
init|=
operator|(
name|DefaultCamelContext
operator|)
name|context
decl_stmt|;
name|InstrumentationLifecycleStrategy
name|ls
init|=
operator|new
name|InstrumentationLifecycleStrategy
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|dc
operator|.
name|setLifecycleStrategy
argument_list|(
name|ls
argument_list|)
expr_stmt|;
name|ls
operator|.
name|onContextCreate
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
comment|//Using the array to hold the busMBeans to avoid the CurrentModificationException
name|Object
index|[]
name|mBeans
init|=
name|mbeans
operator|.
name|toArray
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|name
range|:
name|mBeans
control|)
block|{
name|mbeans
operator|.
name|remove
argument_list|(
operator|(
name|ObjectName
operator|)
name|name
argument_list|)
expr_stmt|;
try|try
block|{
name|unregister
argument_list|(
operator|(
name|ObjectName
operator|)
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|jmex
parameter_list|)
block|{
comment|// log
block|}
block|}
block|}
DECL|method|registerMBeanWithServer (Object obj, ObjectName name, boolean forceRegistration)
specifier|private
name|void
name|registerMBeanWithServer
parameter_list|(
name|Object
name|obj
parameter_list|,
name|ObjectName
name|name
parameter_list|,
name|boolean
name|forceRegistration
parameter_list|)
throws|throws
name|JMException
block|{
name|ObjectInstance
name|instance
init|=
literal|null
decl_stmt|;
try|try
block|{
name|instance
operator|=
name|server
operator|.
name|registerMBean
argument_list|(
name|obj
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InstanceAlreadyExistsException
name|e
parameter_list|)
block|{
if|if
condition|(
name|forceRegistration
condition|)
block|{
name|server
operator|.
name|unregisterMBean
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|instance
operator|=
name|server
operator|.
name|registerMBean
argument_list|(
name|obj
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
name|mbeans
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

