begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
package|;
end_package

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

begin_class
DECL|class|TestStoredFunction
specifier|public
specifier|final
class|class
name|TestStoredFunction
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
name|TestStoredFunction
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|TestStoredFunction ()
specifier|private
name|TestStoredFunction
parameter_list|()
block|{     }
DECL|method|subnumbers (int val1, int val2)
specifier|public
specifier|static
name|int
name|subnumbers
parameter_list|(
name|int
name|val1
parameter_list|,
name|int
name|val2
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"calling subnumbers:{} - {}"
argument_list|,
name|val1
argument_list|,
name|val2
argument_list|)
expr_stmt|;
return|return
name|val1
operator|-
name|val2
return|;
block|}
block|}
end_class

end_unit

