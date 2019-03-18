begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.beanstalk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|beanstalk
package|;
end_package

begin_enum
DECL|enum|BeanstalkCommand
specifier|public
enum|enum
name|BeanstalkCommand
block|{
DECL|enumConstant|bury
DECL|enumConstant|release
DECL|enumConstant|put
DECL|enumConstant|touch
DECL|enumConstant|delete
DECL|enumConstant|kick
name|bury
block|,
name|release
block|,
name|put
block|,
name|touch
block|,
name|delete
block|,
name|kick
block|}
end_enum

end_unit

