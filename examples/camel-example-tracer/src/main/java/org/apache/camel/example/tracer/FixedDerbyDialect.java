begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.tracer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|tracer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hibernate
operator|.
name|dialect
operator|.
name|DerbyDialect
import|;
end_import

begin_comment
comment|/**  * See https://hibernate.onjira.com/browse/HHH-7264 for details  *  */
end_comment

begin_class
DECL|class|FixedDerbyDialect
specifier|public
class|class
name|FixedDerbyDialect
extends|extends
name|DerbyDialect
block|{
DECL|method|FixedDerbyDialect ()
specifier|public
name|FixedDerbyDialect
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|registerColumnType
argument_list|(
name|Types
operator|.
name|CLOB
argument_list|,
literal|"clob"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

