begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.scan.b
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|scan
operator|.
name|b
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
name|spring
operator|.
name|scan
operator|.
name|ScannableOne
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
name|spring
operator|.
name|scan
operator|.
name|a
operator|.
name|ScanTargetOne
import|;
end_import

begin_class
annotation|@
name|ScannableOne
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
DECL|class|ScanTargetTwo
specifier|public
class|class
name|ScanTargetTwo
extends|extends
name|ScanTargetOne
block|{
DECL|method|someMethod ()
specifier|public
name|void
name|someMethod
parameter_list|()
block|{     }
block|}
end_class

end_unit

