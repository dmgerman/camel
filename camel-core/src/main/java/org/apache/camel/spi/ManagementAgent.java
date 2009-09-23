begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Service
import|;
end_import

begin_comment
comment|/**  * Camel JMX service agent  */
end_comment

begin_interface
DECL|interface|ManagementAgent
specifier|public
interface|interface
name|ManagementAgent
extends|extends
name|Service
block|{
comment|/**      * Registers object with management infrastructure with a specific name. Object must be annotated or       * implement standard MBean interface.      *      * @param obj  the object to register      * @param name the name      * @throws JMException is thrown if the registration failed      */
DECL|method|register (Object obj, ObjectName name)
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
function_decl|;
comment|/**      * Registers object with management infrastructure with a specific name. Object must be annotated or       * implement standard MBean interface.      *      * @param obj  the object to register      * @param name the name      * @param forceRegistration if set to<tt>true</tt>, then object will be registered despite      * existing object is already registered with the name.      * @throws JMException is thrown if the registration failed      */
DECL|method|register (Object obj, ObjectName name, boolean forceRegistration)
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
function_decl|;
comment|/**      * Unregisters object based upon registered name      *      * @param name the name      * @throws JMException is thrown if the unregistration failed      */
DECL|method|unregister (ObjectName name)
name|void
name|unregister
parameter_list|(
name|ObjectName
name|name
parameter_list|)
throws|throws
name|JMException
function_decl|;
comment|/**      * Is the given object registered      *      * @param name the name      * @return<tt>true</tt> if registered      */
DECL|method|isRegistered (ObjectName name)
name|boolean
name|isRegistered
parameter_list|(
name|ObjectName
name|name
parameter_list|)
function_decl|;
comment|/**      * Get the MBeanServer which hosts managed objects.      *<p/>      *<b>Notice:</b> If the JMXEnabled configuration is not set to<tt>true</tt>,      * this method will return<tt>null</tt>.      *       * @return the MBeanServer      */
DECL|method|getMBeanServer ()
name|MBeanServer
name|getMBeanServer
parameter_list|()
function_decl|;
comment|/**      * Sets a custom mbean server to use      *      * @param mbeanServer the custom mbean server      */
DECL|method|setMBeanServer (MBeanServer mbeanServer)
name|void
name|setMBeanServer
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|)
function_decl|;
comment|/**      * Get domain name for Camel MBeans.      *<p/>      *<b>Notice:</b> That this can be different that the default domain name of the MBean Server.      *       * @return domain name      */
DECL|method|getMBeanObjectDomainName ()
name|String
name|getMBeanObjectDomainName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

