begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|ApplicationScoped
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|util
operator|.
name|AnnotationLiteral
import|;
end_import

begin_class
annotation|@
name|Vetoed
DECL|class|ApplicationScopedLiteral
specifier|final
class|class
name|ApplicationScopedLiteral
extends|extends
name|AnnotationLiteral
argument_list|<
name|ApplicationScoped
argument_list|>
implements|implements
name|ApplicationScoped
block|{
DECL|field|APPLICATION_SCOPED
specifier|static
specifier|final
name|ApplicationScoped
name|APPLICATION_SCOPED
init|=
operator|new
name|ApplicationScopedLiteral
argument_list|()
decl_stmt|;
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|ApplicationScopedLiteral ()
specifier|private
name|ApplicationScopedLiteral
parameter_list|()
block|{     }
block|}
end_class

end_unit

