begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A bunch of useful testing methods  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|TestSupport
specifier|public
specifier|abstract
class|class
name|TestSupport
extends|extends
name|TestCase
block|{
DECL|field|log
specifier|protected
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|assertIsInstanceOf (Class<T> expectedType, Object value)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|assertIsInstanceOf
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|expectedType
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Expected an instance of type: "
operator|+
name|expectedType
operator|.
name|getName
argument_list|()
operator|+
literal|" but was null"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"object should be a "
operator|+
name|expectedType
operator|.
name|getName
argument_list|()
operator|+
literal|" but was: "
operator|+
name|value
operator|+
literal|" with type: "
operator|+
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|expectedType
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|expectedType
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

