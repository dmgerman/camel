begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|enterprise
operator|.
name|util
operator|.
name|AnnotationLiteral
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Qualifier
import|;
end_import

begin_annotation_defn
annotation|@
name|Qualifier
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
block|,
name|ElementType
operator|.
name|METHOD
block|,
name|ElementType
operator|.
name|FIELD
block|,
name|ElementType
operator|.
name|PARAMETER
block|}
argument_list|)
DECL|annotation|Startup
annotation_defn|@interface
name|Startup
block|{
annotation|@
name|Vetoed
DECL|class|Literal
specifier|final
class|class
name|Literal
extends|extends
name|AnnotationLiteral
argument_list|<
name|Startup
argument_list|>
implements|implements
name|Startup
block|{
DECL|field|STARTUP
specifier|static
specifier|final
name|Startup
name|STARTUP
init|=
operator|new
name|Literal
argument_list|()
decl_stmt|;
DECL|method|Literal ()
specifier|private
name|Literal
parameter_list|()
block|{         }
block|}
block|}
end_annotation_defn

end_unit

