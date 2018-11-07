begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlEnum
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_comment
comment|/**  * Enumerates all saga propagation modes.  */
end_comment

begin_enum
annotation|@
name|XmlType
annotation|@
name|XmlEnum
DECL|enum|SagaPropagation
specifier|public
enum|enum
name|SagaPropagation
block|{
comment|/**      * Join the existing saga or create a new one if it does not exist.      */
DECL|enumConstant|REQUIRED
name|REQUIRED
block|,
comment|/**      * Always create a new saga. Suspend the old saga and resume it when the new one terminates.      */
DECL|enumConstant|REQUIRES_NEW
name|REQUIRES_NEW
block|,
comment|/**      * A saga must be already present. The existing saga is joined.      */
DECL|enumConstant|MANDATORY
name|MANDATORY
block|,
comment|/**      * If a saga already exists, then join it.      */
DECL|enumConstant|SUPPORTS
name|SUPPORTS
block|,
comment|/**      * If a saga already exists, it is suspended and resumed when the current block completes.      */
DECL|enumConstant|NOT_SUPPORTED
name|NOT_SUPPORTED
block|,
comment|/**      * The current block must never be invoked within a saga.      */
DECL|enumConstant|NEVER
name|NEVER
block|}
end_enum

end_unit

