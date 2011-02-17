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

begin_comment
comment|/**  * An interface to represent an object being managed.  *<p/>  * This allows you to gain fine grained control of managing objects with Camel.  * For example various Camel components will implement this interface to provide  * management to their endpoints and consumers.  *<p/>  * A popular choice is to use Spring JMX annotations to decorate your object to pinpoint  * the JMX attributes and operations. If you do this then you do<b>not</b> need to use  * this interface. This interface is only if you need to be in full control of the MBean  * and therefore can return a {@link javax.management.MBeanInfo} object.  *  * @version   * @deprecated use Spring JMX annotations,  *             see this<a href="http://camel.apache.org/why-is-my-processor-not-showing-up-in-jconsole.html">FAQ entry</a>  */
end_comment

begin_interface
annotation|@
name|Deprecated
DECL|interface|ManagementAware
specifier|public
interface|interface
name|ManagementAware
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Gets the managed object      *      * @param object the object to be managed      * @return the managed object      */
DECL|method|getManagedObject (T object)
name|Object
name|getManagedObject
parameter_list|(
name|T
name|object
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

