begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsHelper
specifier|public
specifier|final
class|class
name|JmsHelper
block|{
DECL|field|DEFAULT_QUEUE_BROWSE_STRATEGY
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_QUEUE_BROWSE_STRATEGY
init|=
literal|"org.apache.camel.component.jms.DefaultQueueBrowseStrategy"
decl_stmt|;
DECL|method|JmsHelper ()
specifier|private
name|JmsHelper
parameter_list|()
block|{
comment|// utility class
block|}
comment|/**      * Is the spring version 2.0.x?      *      * @return<tt>true</tt> if 2.0.x or<tt>false</tt> if newer such as 2.5.x      */
DECL|method|isSpring20x ()
specifier|public
specifier|static
name|boolean
name|isSpring20x
parameter_list|()
block|{
comment|// this class is only possible to instantiate in 2.5.x or newer
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|DEFAULT_QUEUE_BROWSE_STRATEGY
argument_list|,
name|JmsComponent
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|ObjectHelper
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|e
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

