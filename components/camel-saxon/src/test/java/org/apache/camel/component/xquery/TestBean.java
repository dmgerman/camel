begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xquery
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xquery
package|;
end_package

begin_class
DECL|class|TestBean
specifier|public
class|class
name|TestBean
block|{
DECL|field|subject
specifier|private
name|String
name|subject
decl_stmt|;
DECL|method|onMethod (@QueryR)String subject)
specifier|public
name|void
name|onMethod
parameter_list|(
annotation|@
name|XQuery
argument_list|(
literal|"/*/@subject"
argument_list|)
name|String
name|subject
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
DECL|method|getSubject ()
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
block|}
end_class

end_unit

