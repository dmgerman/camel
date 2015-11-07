begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.springboot.commands.crsh
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|springboot
operator|.
name|commands
operator|.
name|crsh
package|;
end_package

begin_import
import|import
name|org
operator|.
name|crsh
operator|.
name|cli
operator|.
name|Argument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|crsh
operator|.
name|cli
operator|.
name|Man
import|;
end_import

begin_import
import|import
name|org
operator|.
name|crsh
operator|.
name|cli
operator|.
name|Usage
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

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Usage
argument_list|(
literal|"Camel context name"
argument_list|)
annotation|@
name|Man
argument_list|(
literal|"Camel context name"
argument_list|)
annotation|@
name|Argument
argument_list|(
name|name
operator|=
literal|"Camel Context"
argument_list|,
name|completer
operator|=
name|CamelCompleter
operator|.
name|class
argument_list|)
DECL|annotation|ArgumentCamelContext
annotation_defn|@interface
name|ArgumentCamelContext
block|{}
end_annotation_defn

end_unit

