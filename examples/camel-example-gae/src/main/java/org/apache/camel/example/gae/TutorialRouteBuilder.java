begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.gae
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|gae
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
name|Exchange
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
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|gae
operator|.
name|mail
operator|.
name|GMailBinding
import|;
end_import

begin_class
DECL|class|TutorialRouteBuilder
specifier|public
class|class
name|TutorialRouteBuilder
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"ghttp:///weather"
argument_list|)
operator|.
name|to
argument_list|(
literal|"gtask://default"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|constant
argument_list|(
literal|"text/plain"
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Weather report will be sent to "
argument_list|)
operator|.
name|append
argument_list|(
name|header
argument_list|(
literal|"mailto"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"gtask://default"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
name|constant
argument_list|(
literal|"weather="
argument_list|)
operator|.
name|append
argument_list|(
name|header
argument_list|(
literal|"city"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"ghttp://www.google.com/ig/api"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|WeatherProcessor
argument_list|()
argument_list|)
operator|.
name|setHeader
argument_list|(
name|GMailBinding
operator|.
name|GMAIL_SUBJECT
argument_list|,
name|constant
argument_list|(
literal|"Weather report"
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|GMailBinding
operator|.
name|GMAIL_TO
argument_list|,
name|header
argument_list|(
literal|"mailto"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"gmail://krasserm@googlemail.com"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

