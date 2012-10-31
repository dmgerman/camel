begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Documented
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Inherited
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * Indicates to set the shutdown timeout of all {@code CamelContext}s instantiated through the   * use of Spring Test loaded application contexts.  If no annotation is used, the timeout is  * automatically reduced to 10 seconds by the test framework.  If the annotation is present the  * shutdown timeout is set based on the value of {@link #value()}.   */
end_comment

begin_annotation_defn
annotation|@
name|Documented
annotation|@
name|Inherited
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|TYPE
block|}
argument_list|)
DECL|annotation|ShutdownTimeout
specifier|public
annotation_defn|@interface
name|ShutdownTimeout
block|{
comment|/**      * The shutdown timeout to set on the {@code CamelContext}(s).      * Defaults to {@code 10} seconds.      */
DECL|method|value ()
name|int
name|value
parameter_list|()
default|default
literal|10
function_decl|;
comment|/**      * The time unit that {@link #value()} is in.      */
DECL|method|timeUnit ()
DECL|field|TimeUnit.SECONDS
name|TimeUnit
name|timeUnit
parameter_list|()
default|default
name|TimeUnit
operator|.
name|SECONDS
function_decl|;
block|}
end_annotation_defn

end_unit

