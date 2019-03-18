begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|Descriptor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|DynamicMBean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanOperationInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ReflectionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|RuntimeOperationsException
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
name|URISupport
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
comment|/**  * A {@link javax.management.modelmbean.RequiredModelMBean} which allows us to intercept invoking operations on the MBean.  *<p/>  * This allows us to intercept calls to custom mbeans where allows us to mix-in the standard set of mbean attributes  * and operations that Camel provides out of the box.  *<p/>  * For example if mask has been enabled on JMX, then we use this implementation  * to hide sensitive information from the returned JMX attributes / operations.  */
end_comment

begin_class
DECL|class|MixinRequiredModelMBean
specifier|public
class|class
name|MixinRequiredModelMBean
extends|extends
name|RequiredModelMBean
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
name|MixinRequiredModelMBean
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|mask
specifier|private
name|boolean
name|mask
decl_stmt|;
DECL|field|defaultMbi
specifier|private
name|ModelMBeanInfo
name|defaultMbi
decl_stmt|;
DECL|field|defaultObject
specifier|private
name|DynamicMBean
name|defaultObject
decl_stmt|;
DECL|method|MixinRequiredModelMBean ()
specifier|public
name|MixinRequiredModelMBean
parameter_list|()
throws|throws
name|MBeanException
throws|,
name|RuntimeOperationsException
block|{
comment|// must have default no-arg constructor
block|}
DECL|method|MixinRequiredModelMBean (ModelMBeanInfo mbi, boolean mask, ModelMBeanInfo defaultMbi, DynamicMBean defaultObject)
specifier|public
name|MixinRequiredModelMBean
parameter_list|(
name|ModelMBeanInfo
name|mbi
parameter_list|,
name|boolean
name|mask
parameter_list|,
name|ModelMBeanInfo
name|defaultMbi
parameter_list|,
name|DynamicMBean
name|defaultObject
parameter_list|)
throws|throws
name|MBeanException
throws|,
name|RuntimeOperationsException
block|{
name|super
argument_list|(
name|mbi
argument_list|)
expr_stmt|;
name|this
operator|.
name|mask
operator|=
name|mask
expr_stmt|;
name|this
operator|.
name|defaultMbi
operator|=
name|defaultMbi
expr_stmt|;
name|this
operator|.
name|defaultObject
operator|=
name|defaultObject
expr_stmt|;
block|}
DECL|method|isMask ()
specifier|public
name|boolean
name|isMask
parameter_list|()
block|{
return|return
name|mask
return|;
block|}
annotation|@
name|Override
DECL|method|invoke (String opName, Object[] opArgs, String[] sig)
specifier|public
name|Object
name|invoke
parameter_list|(
name|String
name|opName
parameter_list|,
name|Object
index|[]
name|opArgs
parameter_list|,
name|String
index|[]
name|sig
parameter_list|)
throws|throws
name|MBeanException
throws|,
name|ReflectionException
block|{
name|Object
name|answer
decl_stmt|;
if|if
condition|(
name|defaultMbi
operator|!=
literal|null
operator|&&
name|defaultObject
operator|!=
literal|null
operator|&&
name|isDefaultOperation
argument_list|(
name|opName
argument_list|)
condition|)
block|{
name|answer
operator|=
name|defaultObject
operator|.
name|invoke
argument_list|(
name|opName
argument_list|,
name|opArgs
argument_list|,
name|sig
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|super
operator|.
name|invoke
argument_list|(
name|opName
argument_list|,
name|opArgs
argument_list|,
name|sig
argument_list|)
expr_stmt|;
block|}
comment|// mask the answer if enabled and it was a String type (we cannot mask other types)
if|if
condition|(
name|mask
operator|&&
name|answer
operator|instanceof
name|String
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|answer
argument_list|)
operator|&&
name|isMaskOperation
argument_list|(
name|opName
argument_list|)
condition|)
block|{
name|answer
operator|=
name|mask
argument_list|(
name|opName
argument_list|,
operator|(
name|String
operator|)
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|isDefaultOperation (String opName)
specifier|protected
name|boolean
name|isDefaultOperation
parameter_list|(
name|String
name|opName
parameter_list|)
block|{
for|for
control|(
name|MBeanOperationInfo
name|info
range|:
name|defaultMbi
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|info
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|opName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|isMaskOperation (String opName)
specifier|protected
name|boolean
name|isMaskOperation
parameter_list|(
name|String
name|opName
parameter_list|)
block|{
for|for
control|(
name|MBeanOperationInfo
name|info
range|:
name|getMBeanInfo
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|info
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|opName
argument_list|)
condition|)
block|{
name|Descriptor
name|desc
init|=
name|info
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|desc
operator|!=
literal|null
condition|)
block|{
name|Object
name|val
init|=
name|desc
operator|.
name|getFieldValue
argument_list|(
literal|"mask"
argument_list|)
decl_stmt|;
return|return
name|val
operator|!=
literal|null
operator|&&
literal|"true"
operator|.
name|equals
argument_list|(
name|val
argument_list|)
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Masks the returned value from invoking the operation      *      * @param opName  the operation name invoked      * @param value   the current value      * @return the masked value      */
DECL|method|mask (String opName, String value)
specifier|protected
name|String
name|mask
parameter_list|(
name|String
name|opName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
comment|// use sanitize uri which will mask sensitive information
name|String
name|answer
init|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Masking JMX operation: {}.{} value: {} -> {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|getMBeanInfo
argument_list|()
operator|.
name|getClassName
argument_list|()
block|,
name|opName
block|,
name|value
block|,
name|answer
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

