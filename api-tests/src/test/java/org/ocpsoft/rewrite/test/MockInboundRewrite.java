/*
 * Copyright 2011 <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ocpsoft.rewrite.test;

import org.ocpsoft.rewrite.AbstractRewrite;
import org.ocpsoft.rewrite.event.Flow;
import org.ocpsoft.rewrite.event.InboundRewrite;
import org.ocpsoft.rewrite.servlet.event.BaseRewrite.ServletRewriteFlow;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class MockInboundRewrite extends AbstractRewrite implements InboundRewrite
{
   @Override
   public Flow getFlow()
   {
      return ServletRewriteFlow.UN_HANDLED;
   }
}
