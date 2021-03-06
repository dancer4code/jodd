// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package jodd.madvoc.component;

import jodd.madvoc.config.InjectionPoint;
import jodd.madvoc.config.ScopeData;
import jodd.madvoc.meta.In;
import jodd.madvoc.meta.Out;
import jodd.petite.PetiteContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScopeDataInspectorTest {

	static class Action {
		@In String input;
	}

	@Test
	void testInAnnotations() {
		ScopeDataInspector scopeDataInspector = new ScopeDataInspector();
		PetiteContainer madpc = new PetiteContainer();
		scopeDataInspector.scopeResolver = new ScopeResolver();
		scopeDataInspector.scopeResolver.madpc = madpc;

		madpc.addBean("madvocEncoding", new MadvocEncoding());

		ScopeData scopeData = scopeDataInspector.inspectClassScopes(Action.class);

		InjectionPoint[] in1 = scopeData.in();
		InjectionPoint in = in1[0];

		assertEquals("input", in.name());
		assertEquals(String.class, in.type());
	}

	// ----------------------------------------------------------------

	static class BaseAction<A, B> {
		@In A input;
		@Out B output;
	}

	static class GenAction extends BaseAction<String, Integer> {
	}

	@Test
	void testGenericAction() {
		ScopeDataInspector scopeDataInspector = new ScopeDataInspector();
		PetiteContainer madpc = new PetiteContainer();
		scopeDataInspector.scopeResolver = new ScopeResolver();
		scopeDataInspector.scopeResolver.madpc = madpc;
		madpc.addBean("madvocEncoding", new MadvocEncoding());

		ScopeData scopeData = scopeDataInspector.inspectClassScopes(GenAction.class);

		InjectionPoint[] in1 = scopeData.in();
		InjectionPoint[] out1 = scopeData.out();

		InjectionPoint in = in1[0];
		InjectionPoint out = out1[0];

		assertEquals("input", in.name());
		assertEquals(String.class, in.type());

		assertEquals("output", out.name());
		assertEquals(Integer.class, out.type());
	}

}
