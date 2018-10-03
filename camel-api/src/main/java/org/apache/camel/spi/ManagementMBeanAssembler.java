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
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|ModelMBean
import|;
end_import

begin_comment
comment|/**  * An assembler to assemble a {@link javax.management.modelmbean.RequiredModelMBean} which can be used  * to register the object in JMX.  */
end_comment

begin_interface
DECL|interface|ManagementMBeanAssembler
specifier|public
interface|interface
name|ManagementMBeanAssembler
block|{
comment|/**      * Assemble the {@link javax.management.modelmbean.ModelMBean}.      *      * @param mBeanServer the mbean server      * @param obj         the object      * @param name        the object name to use in JMX      * @return the assembled {@link javax.management.modelmbean.ModelMBean}, or<tt>null</tt> if not possible to assemble an MBean      * @throws JMException is thrown if error assembling the mbean      */
DECL|method|assemble (MBeanServer mBeanServer, Object obj, ObjectName name)
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
function_decl|;
block|}
end_interface

end_unit

